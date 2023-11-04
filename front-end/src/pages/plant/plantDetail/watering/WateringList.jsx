import {CContainer} from "@coreui/react";
import getWateringNotificationMsg from "src/utils/function/getWateringNotificationMsg";
import HandleWateringForm from "./HandleWateringForm";
import deleteData from "src/api/backend-api/common/deleteData";
import {useEffect, useState} from "react";
import 'dayjs/locale/ko';
import {Form, Table, notification} from "antd";
import dayjs from "dayjs";
import advancedFormat from 'dayjs/plugin/advancedFormat'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import localeData from 'dayjs/plugin/localeData'
import weekday from 'dayjs/plugin/weekday'
import weekOfYear from 'dayjs/plugin/weekOfYear'
import weekYear from 'dayjs/plugin/weekYear'
import updateData from "src/api/backend-api/common/updateData";
import WateringEditableCell from "./WateringEditableCell";
import getChemicalListForSelect from "../../../../api/service/getChemicalListForSelect";
import getWateringListForTable from "../../../../utils/function/getWateringListForTable";
import getWateringTableColumnArray from "../../../../utils/function/getWateringTableColumnArray";
import getMergedColumns from "../../../../utils/function/getMergedColumns";
import getData from "../../../../api/backend-api/common/getData";
import {useDispatch, useSelector} from "react-redux";

/**
 * 한 식물의 물주기 정보 plant detail 아래쪽 table
 * @param {*} props
 * @returns
 */
const WateringList = ({plantId, setPlant}) => {
  dayjs.extend(customParseFormat)
  dayjs.extend(advancedFormat)
  dayjs.extend(weekday)
  dayjs.extend(localeData)
  dayjs.extend(weekOfYear)
  dayjs.extend(weekYear)

  // 테이블에 넣을 물주기 데이터
  const [waterings, setWaterings] = useState([]);
  const [page, setPage] = useState(1);

  const [editWatering, setEditWatering] = useState({});
  const [chemicalList, setChemicalList] = useState([]);
  const [isWateringFormOpen, setIsWateringFormOpen] = useState(false);

  // 해당 유저의 chemical list
  useEffect(() => {
    console.log("onmount")
    getChemicalListForSelect(setChemicalList);
  }, [])

  useEffect(() => {
    onMountWateringList();
  }, [page])

  const onMountWateringList = async () => {
    const res = await getData(`/plant/${plantId}/watering?page=${page - 1}`);
    setWaterings(() => res);
  }

  // editable rows
  const [form] = Form.useForm();
  const [editingKey, setEditingKey] = useState('');
  const isEditing = (record) => record.id === editingKey;

  const edit = (record) => {
    console.log("edit record === ", record);

    form.setFieldsValue({
      ...record,
      wateringDate: dayjs(new Date(record.wateringDate)),
    });

    setEditWatering({
      plantId: plantId,
      id: record.id,
      wateringDate: record.wateringDate,
      chemicalId: record.chemicalId,
    });

    setEditingKey(record.id);
    setIsWateringFormOpen(false);
  };

  const cancel = () => setEditingKey('');

  const wateringCallBack = (res) => {
    console.log("wateringCallBack res", res);
    setWaterings(res.waterings);
    res.plant && setPlant(res.plant);

    if (res.wateringMsg) {
      const msg = getWateringNotificationMsg(res.wateringMsg.afterWateringCode);
      openNotification(msg);
    }
  }

  const updateWatering = async () => {
    console.log("update watering === editWatering", editWatering);
    const res = await updateData(`/plant/${plantId}/watering/${editWatering.id}?page=${page - 1}`, editWatering);
    wateringCallBack(res);
    setEditingKey('');
  };

  const dispatch = useDispatch();

  const deleteWatering = async (wateringId) => {
    await deleteData(`/plant/${plantId}/watering/${wateringId}`);

    // 마지막 페이지 물주기 하나 남았을 때 삭제하면 한 페이지 앞으로 당김
    if (waterings.length == 1 && page > 1) {
      setPage(() => page - 1);
    }

    setWaterings(waterings.filter(watering => watering.id !== wateringId));
    dispatch({type: 'deleteWatering', payload: null});
  };

  const wateringTableColumnArray = getWateringTableColumnArray(isEditing, updateWatering, editingKey, cancel, edit, deleteWatering);
  const mergedColumns = getMergedColumns(wateringTableColumnArray, "wateringDate", 'date', 'select', isEditing);

  // 물주기 추가/수정/삭제 후 메시지
  const [api, contextHolder] = notification.useNotification();
  const openNotification = (msg) => {

    api.open({
      message: msg.title,
      description: msg.content,
      duration: 4,
    });
  };

  const tableBody = {
    body: {
      cell: (props) => <WateringEditableCell editWatering={editWatering}
                                             setEditWatering={setEditWatering}
                                             chemicalList={chemicalList}
                                             setPlant={setPlant}
                                             {...props} />
    }
  }

  const totalWaterings = useSelector(state => state.waterings.totalWaterings);
  console.log("totalWaterings", totalWaterings);

  return (
    <>
      {contextHolder}

      <CContainer>
        <div className="mt-4 mb-3">
          <HandleWateringForm
            plantId={plantId}
            page={page}
            setWateringList={setWaterings}
            chemicalList={chemicalList}
            isWateringFormOpen={isWateringFormOpen}
            setIsWateringFormOpen={setIsWateringFormOpen}
            setEditingKey={setEditingKey}
            wateringCallBack={wateringCallBack}
          />
        </div>
        <Form form={form} component={false}>
          <Table
            components={tableBody}
            pagination={{onChange: (page) => setPage(() => page), total: totalWaterings}}
            columns={mergedColumns}
            dataSource={getWateringListForTable(waterings)}
          />
        </Form>
      </CContainer>
    </>
  )
}

export default WateringList;
