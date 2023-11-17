import {Table, Form} from 'antd';
import getPlantListForPlantTable from 'src/api/service/getPlantListForPlantTable';
import deleteData from 'src/api/backend-api/common/deleteData';
import getPlantTableColumnArray from "../../../utils/function/getPlantTableColumnArray";
import PlantEditableCell from "./PlantEditableCell";
import React, {useState} from "react";
import getMergedColumns from "../../../utils/function/getMergedColumns";
import updateData from "../../../api/backend-api/common/updateData";
import {useDispatch, useSelector} from "react-redux";

const EditableContext = React.createContext(null);
const EditableRow = ({index, form, ...props}) => {
  return (
    <Form form={form} component={false}>
      <EditableContext.Provider value={form}>
        <tr {...props} />
      </EditableContext.Provider>
    </Form>
  );
};

const PlantTable = () => {
  const plants = useSelector(state => state.plants);
  const places = useSelector(state => state.places.forSelect);
  const dispatch = useDispatch();

  const [form] = Form.useForm();

  const deletePlant = async (plantId) => {
    await deleteData(`/plant/${plantId}`);
    dispatch({type: 'deletePlant', payload: plantId});
  };

  const locale = {
    triggerDesc: '내림차순으로 보기',
    triggerAsc: '오름차순으로 보기',
    cancelSort: '정렬 취소하기'
  }

  const [editingKey, setEditingKey] = useState(0);
  const [modifyPlant, setModifyPlant] = useState({});

  const isEditing = (record) => record.id === editingKey;

  const edit = async (record) => {
    console.log("record", record);

    form.setFieldsValue({
      ...record,
    });

    // 최근 물주기(recentWateringPeriod)가 계산되지 않았으면 ?가 들어있으므로
    // isNaN 메소드를 통해 검사
    setModifyPlant({
      recentWateringPeriod: isNaN(record.recentWateringPeriod) ? 0 : record.recentWateringPeriod,
      species: record.species,
      medium: record.tags.medium
    });

    setEditingKey(record.id);
  };

  const cancel = () => setEditingKey(0);

  const updatePlant = async () => {
    const values = await form.validateFields();

    const res = await updateData(`/plant/${editingKey}`, {...values, ...modifyPlant, id: editingKey});
    dispatch({type: 'updatePlant', payload: res})

    setEditingKey(0);
  }

  const columns = getPlantTableColumnArray(places, isEditing, cancel, edit, editingKey, updatePlant, deletePlant);
  const mergedColumns = getMergedColumns(columns, 'placeName', 'select', 'text', isEditing);

  return (
    <div>
      <Table
        components={{
          body: {
            row: (props) => <EditableRow form={form} {...props} />,
            cell: (props) => <PlantEditableCell
              editingKey={editingKey}
              editableContext={EditableContext}
              placeList={places}
              {...props} />
          }
        }}
        className="new-line"
        style={{width: "inherit"}}
        pagination={{onChange: cancel}}
        locale={locale}
        columns={mergedColumns}
        dataSource={getPlantListForPlantTable(plants)}
      />
    </div>
  )
}

export default PlantTable;
