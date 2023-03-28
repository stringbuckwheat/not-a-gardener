import React, {useState, useEffect} from 'react'
import NoItem from 'src/components/NoItem';
import AddPlantButton from 'src/components/button/AddPlantButton';
import GardenList from "./GardenList";
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";

const Garden = () => {
  const [isLoading, setLoading] = useState(true);
  const [hasPlantList, setHasPlantList] = useState(false);

  // 식물 리스트
  const [plantList, setPlantList] = useState([]);

  const [originPlantList, setOriginPlantList] = useState([]);

  const onMountGarden = async () => {
    const data = await getData("/garden");
    setLoading(false);
    setHasPlantList(data.length > 0);

    data.sort((a, b) => (a.gardenDetail.wateringCode - b.gardenDetail.wateringCode))

    setPlantList(data);
    setOriginPlantList(data);
  }

  useEffect(() => {
    onMountGarden();
  }, [])

  if (isLoading) {
    return <Loading/>
  } else if (!hasPlantList) {
    return <NoItem
      title="아직 정원에 아무도 없네요"
      button={<AddPlantButton size="lg"/>}/>
  } else {
    return <GardenList
      originPlantList={originPlantList}
      plantList={plantList}
      setPlantList={setPlantList}/>
  }
}

export default Garden
