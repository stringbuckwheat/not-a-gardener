import {CContainer} from "@coreui/react";
import getWateringNotificationMsg from "src/utils/function/getWateringNotificationMsg";
import WateringFormOpen from "./WateringFormOpen";
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
import WateringListAction from "./WateringListAction";
import EditableCell from "./EditableCell";
import getChemicalListForSelect from "../../api/service/getChemicalListForSelect";
import getWateringListForTable from "../../utils/function/getWateringListForTable";

/**
 * plant detail 아래쪽에 table로 들어감
 * @param {*} props
 * @returns
 */

const WateringList = (props) => {
  dayjs.extend(customParseFormat)
  dayjs.extend(advancedFormat)
  dayjs.extend(weekday)
  dayjs.extend(localeData)
  dayjs.extend(weekOfYear)
  dayjs.extend(weekYear)

  const {plant, setPlant, wateringList, setWateringList} = props;

  const [editWatering, setEditWatering] = useState({});
  const [chemicalList, setChemicalList] = useState([]);
  const [isWateringFormOpen, setIsWateringFormOpen] = useState(false);

  // 해당 유저의 chemical list
  useEffect(() => {
    getChemicalListForSelect(setChemicalList);
  }, [])

  // editable rows
  const [form] = Form.useForm();
  const [editingKey, setEditingKey] = useState('');
  const isEditing = (record) => record.wateringNo === editingKey;

  const edit = (record) => {
    form.setFieldsValue({
      ...record,
      wateringDate: dayjs(new Date(record.wateringDate)),
    });

    setEditWatering({
      plantNo: plant.plantNo,
      wateringNo: record.wateringNo,
      wateringDate: record.wateringDate,
      chemicalNo: record.chemicalNo,
    });

    setEditingKey(record.wateringNo);
    setIsWateringFormOpen(false);
  };

  const cancel = () => {
    setEditingKey('');
  };

  const updateWatering = async () => {
    const res = await updateData("watering", editWatering.wateringNo, editWatering);
    setWateringList(res.wateringList);

    if (res.plant) {
      setPlant(res.plant);
    }

    if (res.wateringMsg) {
      const msg = getWateringNotificationMsg(res.wateringMsg.wateringCode)
      openNotification(msg);
    }

    setEditingKey('');
  };

  const wateringTableColumnArray = [
    {
      title: '언제',
      dataIndex: 'wateringDate',
      key: 'wateringDate',
      editable: true,
    },
    {
      title: '무엇을',
      dataIndex: 'chemicalName',
      key: 'chemicalName',
      editable: true,
    },
    {
      title: '관수 간격',
      dataIndex: 'wateringPeriod',
      key: 'wateringPeriod',
    },
    {
      title: '',
      key: 'action',
      render: (_, record) => {
        const editable = isEditing(record);

        return <WateringListAction
          record={record}
          editable={editable}
          updateWatering={updateWatering}
          editingKey={editingKey}
          cancel={cancel}
          edit={edit}
          deleteWatering={deleteWatering}/>
      },
    },
  ]

  const mergedColumns = wateringTableColumnArray.map((col) => {
    if (!col.editable) {
      return col;
    }

    return {
      ...col,
      onCell: (record) => ({
        record,
        inputType: col.dataIndex === 'wateringDate' ? 'date' : 'select',
        dataIndex: col.dataIndex,
        title: col.title,
        editing: isEditing(record),
      }),
    };
  });

  const deleteWatering = (wateringNo) => {
    deleteData("/watering", wateringNo);
    setWateringList(wateringList.filter(watering => watering.wateringNo !== wateringNo))
  };

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
      cell: (props) => <EditableCell editWatering={editWatering}
                                     setEditWatering={setEditWatering}
                                     chemicalList={chemicalList}
                                     setPlant={setPlant}
                                     {...props} />
    }
  }

  return (
    <>
      {contextHolder}

      <CContainer>
        <div className="mt-4 mb-3">
          <WateringFormOpen
            isWateringFormOpen={isWateringFormOpen}
            setIsWateringFormOpen={setIsWateringFormOpen}
            plantNo={plant.plantNo}
            openNotification={openNotification}
            wateringList={wateringList}
            setWateringList={setWateringList}
            chemicalList={chemicalList}
            setPlant={setPlant}
            setEditingKey={setEditingKey}
          />
        </div>
        <Form form={form} component={false}>
          <Table
            components={tableBody}
            pagination={{onChange: cancel}}
            rowClassName="editable-row"
            columns={mergedColumns}
            dataSource={getWateringListForTable(wateringList)}
          />
        </Form>
      </CContainer>
    </>
  )
}

export default WateringList;
