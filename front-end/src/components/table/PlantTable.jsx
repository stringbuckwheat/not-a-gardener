import {Table, Form} from 'antd';
import getPlantListForPlantTable from 'src/api/service/getPlantListForPlantTable';
import deleteData from 'src/api/backend-api/common/deleteData';
import getPlantTableColumnArray from "../../utils/function/getPlantTableColumnArray";
import PlantEditableCell from "../../pages/plant/PlantEditableCell";
import {useState} from "react";
import updateData from "../../api/backend-api/common/updateData";
import getMergedColumns from "../../utils/function/getMergedColumns";

const PlantTable = (props) => {
  const {originPlantList, setPlantList, placeList} = props;
  const plantList = getPlantListForPlantTable(originPlantList);

  const deletePlant = (plantNo) => {
    deleteData("/plant", plantNo);
    const afterDelete = originPlantList.filter(plant => plant.plant.plantNo !== plantNo);
    setPlantList(afterDelete);
  };

  const locale = {
    triggerDesc: '내림차순으로 보기',
    triggerAsc: '오름차순으로 보기',
    cancelSort: '정렬 취소하기'
  }

  const [form] = Form.useForm();
  const [modifyPlant, setModifyPlant] = useState({});
  const [editingKey, setEditingKey] = useState('');

  const tableBody = {
    body: {
      cell: (props) => <PlantEditableCell placeList={placeList}
                                          modifyPlant={modifyPlant}
                                          setModifyPlant={setModifyPlant}
                                          {...props} />
    }
  }


  const isEditing = (record) => record.plantNo === editingKey;

  const edit = (record) => {
    form.setFieldsValue({
      ...record,
    });

    setModifyPlant({
      plantNo: record.plantNo,
      placeNo: record.placeNo,
      plantName: record.plantName,
      medium: record.tags.medium,
      plantSpecies: record.plantSpecies,
      averageWateringPeriod: record.averageWateringPeriod,
      birthDay: record.birthDay
    })

    setEditingKey(record.plantNo);
  };

  const cancel = () => {
    setEditingKey('');
  };

  const updatePlant = async () => {
    const res = await updateData("plant", modifyPlant.plantNo, modifyPlant);

    const updatedPlantList = originPlantList.map((plant) => {
      return plant.plant.plantNo === modifyPlant.plantNo ? {...res} : plant;
    })

    setPlantList(updatedPlantList);
    setEditingKey('');
  }

  const columns = getPlantTableColumnArray(placeList, isEditing, cancel, edit, editingKey, updatePlant, deletePlant);
  const mergedColumns = getMergedColumns(columns, 'placeName', 'select', 'text', isEditing);

  return (
    <div className="mt-3">
      <Form form={form} component={false}>
        <Table
          components={tableBody}
          className="mt-3 new-line"
          pagination={{onChange: cancel}}
          locale={locale}
          columns={mergedColumns}
          dataSource={plantList}
        />
      </Form>
    </div>
  )
}

export default PlantTable;
