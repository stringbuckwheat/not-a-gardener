import deleteData from "src/api/backend-api/common/deleteData";
import {useEffect, useState} from "react";
import 'dayjs/locale/ko';
import {Form, Table} from "antd";
import dayjs from "dayjs";
import advancedFormat from 'dayjs/plugin/advancedFormat'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import localeData from 'dayjs/plugin/localeData'
import weekday from 'dayjs/plugin/weekday'
import weekOfYear from 'dayjs/plugin/weekOfYear'
import weekYear from 'dayjs/plugin/weekYear'
import updateData from "src/api/backend-api/common/updateData";
import WateringEditableCell from "./WateringEditableCell";
import getWateringTableColumnArray from "../../../utils/function/getWateringTableColumnArray";
import getMergedColumns from "../../../utils/function/getMergedColumns";
import getData from "../../../api/backend-api/common/getData";
import {useDispatch, useSelector} from "react-redux";
import {useParams} from "react-router-dom";
import PlantDetailAction from "../../../redux/reducer/plant_detail/plantDetailAction";
import WateringAction from "../../../redux/reducer/waterings/wateringAction";

const getWateringListForTable = (wateringList) => {
  return wateringList?.map((watering) => {
      return (
        {
          ...watering,
          period: watering.period == 0
            ? "첫 기록!"
            : `${watering.period}일`
        }
      )
    }
  )
}

/**
 * 한 식물의 물주기 정보 plant plant_detail 아래쪽 table
 * @param {*} props
 * @returns
 */
const WateringList = ({openNotification, wateringCallBack}) => {
  dayjs.extend(customParseFormat)
  dayjs.extend(advancedFormat)
  dayjs.extend(weekday)
  dayjs.extend(localeData)
  dayjs.extend(weekOfYear)
  dayjs.extend(weekYear)

  const dispatch = useDispatch();
  const plantId = useParams().plantId;

  const waterings = useSelector(state => state.plantDetail.waterings);
  const totalWaterings = useSelector(state => state.waterings.totalWaterings);

  // 테이블에 넣을 물주기 데이터
  const [page, setPage] = useState(1);
  const [editWatering, setEditWatering] = useState({plantId, chemicalId: 0});
  const [form] = Form.useForm();
  const [editingKey, setEditingKey] = useState('');

  useEffect(() => {
    onMountWateringList();
  }, [page])

  const onMountWateringList = async () => {
    const res = await getData(`/plant/${plantId}/watering?page=${page - 1}`);
    dispatch({type: PlantDetailAction.FETCH_WATERING, payload: res});

    const chemicals = await getData("/chemical");
    dispatch({type: PlantDetailAction.SET_CHEMICALS_FOR_SELECT, payload: chemicals});
  }

  // editable rows
  const isEditing = (record) => record.id === editingKey;

  const edit = (record) => {
    form.setFieldsValue({
      ...record,
      wateringDate: dayjs(new Date(record.wateringDate)),
    });

    setEditWatering({
      plantId: plantId,
      id: record.id,
      wateringDate: record.wateringDate,
      chemicalId: record.chemicalId ?? 0,
    });

    setEditingKey(record.id);
    dispatch({type: PlantDetailAction.SET_WATERING_FORM_OPENED, payload: false});
  };

  const cancel = () => setEditingKey('');

  const updateWatering = async () => {
    console.log("update watering", editWatering);

    const res = await updateData(`/plant/${plantId}/watering/${editWatering.id}?page=${page - 1}`, editWatering);
    wateringCallBack(res);
    setEditingKey('');
  };

  const deleteWatering = async (wateringId) => {
    await deleteData(`/plant/${plantId}/watering/${wateringId}`);

    // 마지막 페이지 물주기 하나 남았을 때 삭제하면 한 페이지 앞으로 당김
    if (waterings.length == 1 && page > 1) {
      setPage(() => page - 1);
    }

    dispatch({type: WateringAction.DELETE_ONE_IN_TOTAL_WATERING, payload: null});
    dispatch({type: PlantDetailAction.DELETE_WATERING, payload: wateringId});
  };

  const wateringTableColumnArray = getWateringTableColumnArray(isEditing, updateWatering, editingKey, cancel, edit, deleteWatering);
  const mergedColumns = getMergedColumns(wateringTableColumnArray, "wateringDate", 'date', 'select', isEditing);


  const tableBody = {
    body: {
      cell: (props) => <WateringEditableCell editWatering={editWatering}
                                             setEditWatering={setEditWatering}
                                             {...props} />
    }
  }

  return (
    <>
      <Form form={form} component={false}>
        <Table
          components={tableBody}
          pagination={{onChange: (page) => setPage(() => page), total: totalWaterings}}
          columns={mergedColumns}
          dataSource={getWateringListForTable(waterings)}
        />
      </Form>
    </>
  )
}

export default WateringList;
