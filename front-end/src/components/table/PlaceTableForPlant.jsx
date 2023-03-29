import {Popconfirm, Space, Table, Tag} from 'antd';
import {useState} from 'react';
import ModifyPlantPlaceForm from '../form/ModifyPlantPlaceForm';
import AddPlantInPlaceBtns from '../form/AddPlantInPlaceBtns';
import {Link} from "react-router-dom";
import {DeleteOutlined} from "@ant-design/icons";
import getPlantListForPlacePlantTable from "../../utils/function/getPlantListForPlacePlantTable";
import deleteData from "../../api/backend-api/common/deleteData";
import plantTableColArrInPlace from "../../utils/dataArray/plantTableColArrInPlace";

const PlaceTableForPlant = (props) => {
  const {plantList, setPlantList} = props;

  // select
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);

  const onSelectChange = (newSelectedRowKeys) => {
    setSelectedRowKeys(newSelectedRowKeys);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  const hasSelected = selectedRowKeys.length > 0;

  const deletePlant = (plantNo) => {
    deleteData("/plant", plantNo);
    const deletedList = plantList.filter(plant => plant.plantNo !== plantNo);
    setPlantList(deletedList);
  }

  return (
    <div className="mt-4">
      {
        hasSelected
          ? <ModifyPlantPlaceForm
            selectedPlantNo={selectedRowKeys}
            setSelectedRowKeys={setSelectedRowKeys}
          />
          : <AddPlantInPlaceBtns
            plantList={plantList}
            setPlantList={setPlantList}/>
      }
      <Table
        columns={plantTableColArrInPlace(deletePlant)}
        dataSource={getPlantListForPlacePlantTable(plantList)}
        rowSelection={rowSelection} // for multi-select
      />
    </div>
  )
}

export default PlaceTableForPlant;
