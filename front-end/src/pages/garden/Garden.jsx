import React, {useState, useEffect} from 'react'
import {CRow} from '@coreui/react';
import NoItem from 'src/components/NoItem';
import AddPlantButton from 'src/components/button/AddPlantButton';
import GardenList from "./GardenList";
import getData from "../../api/backend-api/common/getData";

const Garden = () => {
  const [hasPlantList, setHasPlantList] = useState(false);

  // 식물 리스트
  const [plantList, setPlantList] = useState([]);

  const [originPlantList, setOriginPlantList] = useState([]);

  const onMountGarden = async () => {
    const data = await getData("/garden");

    setHasPlantList(data.length > 0);

    data.sort((a, b) => (a.gardenDetail.wateringCode - b.gardenDetail.wateringCode))

    setPlantList(data);
    setOriginPlantList(data);
  }

  useEffect(() => {
    onMountGarden();
  }, [])


  return hasPlantList ? (
    <GardenList
      originPlantList={originPlantList}
      plantList={plantList}
      setPlantList={setPlantList}/>
  ) : (
    <NoItem
      title="아직 정원에 아무도 없네요"
      button={<AddPlantButton size="lg"/>}/>
  )
}

export default Garden
