import {Table} from 'antd';
import {useState} from 'react';
import ChangePlaceOfPlantOnPlace from './ChangePlaceOfPlantOnPlace';
import AddPlantInPlaceButtons from './AddPlantInPlaceButtons';
import getPlantListForPlacePlantTable from "../../../utils/function/getPlantListForPlacePlantTable";
import deleteData from "../../../api/backend-api/common/deleteData";
import getPlantTableColArrInPlace from "../../../utils/function/getPlantTableColArrInPlace";

/**
 * 장소 페이지 하단, 이 장소에 속한 식물들
 * -> ChangePlaceOfPlantOnPlace || AddPlantInPlaceButtons
 * @param plantList
 * @param setPlantList
 * @param placeName
 * @returns {JSX.Element}
 * @constructor
 */
const PlaceTableForPlant = ({plantList, setPlantList, placeName}) => {
  // select
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);

  const onSelectChange = (newSelectedRowKeys) => {
    setSelectedRowKeys(newSelectedRowKeys);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  // 체크박스 눌렀는지
  const hasSelected = selectedRowKeys.length > 0;

  const deletePlant = async (plantId) => {
    console.log("deletePlant");
    console.log("plantId", plantId);
    await deleteData(`/plant/${plantId}`);
    const deletedList = plantList.filter(plant => plant.id !== plantId);
    console.log("deletedList", deletedList);
    setPlantList(() => deletedList);
  }

  return (
    <div className="mt-4">
      {
        hasSelected
          ? <ChangePlaceOfPlantOnPlace
            selectedPlantId={selectedRowKeys}
            setSelectedRowKeys={setSelectedRowKeys}
          />
          : <AddPlantInPlaceButtons
            placeName={placeName}
            plantList={plantList}
            setPlantList={setPlantList}/>
      }
      <Table
        columns={getPlantTableColArrInPlace(deletePlant)}
        dataSource={getPlantListForPlacePlantTable(plantList)}
        rowSelection={rowSelection} // for multi-select
      />
    </div>
  )
}

export default PlaceTableForPlant;
