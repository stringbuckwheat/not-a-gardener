import {Flex, Pagination, Table} from 'antd';
import {useEffect, useState} from 'react';
import ChangePlaceOfPlantOnPlace from './ChangePlaceOfPlantOnPlace';
import AddPlantInPlaceButtons from './AddPlantInPlaceButtons';
import getPlantListForPlacePlantTable from "../../../utils/function/getPlantListForPlacePlantTable";
import deleteData from "../../../api/backend-api/common/deleteData";
import getPlantTableColArrInPlace from "../../../utils/function/getPlantTableColArrInPlace";
import getData from "../../../api/backend-api/common/getData";
import wateringTableColumnArray from "../../../utils/dataArray/wateringTableColumnInChemicalArray";
import TableWithPage from "../../../components/data/TableWithPage";
import {useParams} from "react-router-dom";

/**
 * 장소 페이지 하단, 이 장소에 속한 식물들
 * -> ChangePlaceOfPlantOnPlace || AddPlantInPlaceButtons
 * @param plantList
 * @param setPlantList
 * @param placeName
 * @returns {JSX.Element}
 * @constructor
 */
const PlaceTableForPlant = ({placeName, plantListSize}) => {
  const placeId = useParams().placeId;

  // select
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [plants, setPlants] = useState([]);

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
    const deletedList = plants.filter(plant => plant.id !== plantId);
    console.log("deletedList", deletedList);
    setPlants(() => deletedList);
  }

  const onMountPlaceTableForPlant = async (page) => {
    const plantList = (await getData(`/place/${placeId}/plant?page=${page - 1}`));
    setPlants(() => getPlantListForPlacePlantTable(plantList));
  }

  useEffect(() => {
    console.log("placeId changed")
    onMountPlaceTableForPlant();
  }, [placeId]);

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
            setPlants={setPlants}
          />
      }

      <Table
        rowSelection={rowSelection}
        pagination={{onChange: (page) => onMountPlaceTableForPlant(page), total: plantListSize}}
        columns={getPlantTableColArrInPlace(deletePlant)}
        dataSource={plants}/>
    </div>
  )
}

export default PlaceTableForPlant;
