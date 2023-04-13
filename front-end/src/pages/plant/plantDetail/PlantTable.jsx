import {Table, Form} from 'antd';
import getPlantListForPlantTable from 'src/api/service/getPlantListForPlantTable';
import deleteData from 'src/api/backend-api/common/deleteData';
import getPlantTableColumnArray from "../../../utils/function/getPlantTableColumnArray";
import PlantEditableCell from "./PlantEditableCell";
import React, {useState} from "react";
import getMergedColumns from "../../../utils/function/getMergedColumns";
import updateData from "../../../api/backend-api/common/updateData";

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

/**
 * 전체 식물 리스트
 * @param originPlantList
 * @param setPlantList
 * @param placeList
 * @returns {JSX.Element}
 * @constructor
 */
const PlantTable = ({originPlantList, setPlantList, placeList}) => {
  const [form] = Form.useForm();
  const plantList = getPlantListForPlantTable(originPlantList);

  const deletePlant = async (plantNo) => {
    await deleteData("/plant", plantNo);
    const afterDelete = originPlantList.filter(plant => plant.plant.plantNo !== plantNo);
    setPlantList(afterDelete);
  };

  const locale = {
    triggerDesc: '내림차순으로 보기',
    triggerAsc: '오름차순으로 보기',
    cancelSort: '정렬 취소하기'
  }

  const [editingKey, setEditingKey] = useState(0);
  const [modifyPlant, setModifyPlant] = useState({});

  const isEditing = (record) => record.plantNo === editingKey;

  const edit = async (record) => {
    form.setFieldsValue({
      ...record,
    });

    setModifyPlant({
      averageWateringPeriod: record.averageWateringPeriod,
      plantSpecies: record.plantSpecies,
      medium: record.tags.medium
    });

    setEditingKey(record.plantNo);
  };

  const cancel = () => {
    setEditingKey(0);
  };

  const updatePlant = async () => {
    const values = await form.validateFields();

    const res = await updateData("/plant", editingKey, {...values, ...modifyPlant, plantNo: editingKey});

    const updatedPlantList = originPlantList.map((plant) => {
      return plant.plant.plantNo === editingKey ? {...res} : plant;
    })

    setPlantList(updatedPlantList);
    setEditingKey(0);
  }

  const columns = getPlantTableColumnArray(placeList, isEditing, cancel, edit, editingKey, updatePlant, deletePlant);
  const mergedColumns = getMergedColumns(columns, 'placeName', 'select', 'text', isEditing);

  return (
    <div className="mt-3">
      <Table
        components={{
          body: {
            row: (props) => <EditableRow form={form} {...props} />,
            cell: (props) => <PlantEditableCell
              editingKey={editingKey}
              updatePlant={updatePlant}
              editableContext={EditableContext}
              placeList={placeList}
              {...props} />
          }
        }}
        className="mt-3 new-line"
        pagination={{onChange: cancel}}
        locale={locale}
        columns={mergedColumns}
        dataSource={plantList}
      />
    </div>
  )
}

export default PlantTable;
