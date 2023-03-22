import {useEffect, useState} from "react";
import onMountWithLength from "src/api/service/onMountWithLength";
import AddPlantButton from "src/components/button/AddPlantButton";
import NoItem from "src/components/NoItem";
import PlantList from "./PlantList";
import getData from "../../api/backend-api/common/getData";

const Plant = () => {
  const [hasPlant, setHasPlant] = useState(false);
  const [originPlantList, setOriginPlantList] = useState([])
  const [plantList, setPlantList] = useState([]);

  const onMountPlant = async () => {
    const data = await getData("/garden");

    setHasPlant(data.length > 0);
    setPlantList(data);
    setOriginPlantList(data);
  }

  useEffect(() => {
    onMountPlant();
  }, [])

  return (
    <>
      {
        !hasPlant
          ? <NoItem title="등록된 식물이 없어요" button={<AddPlantButton addUrl="/plant/add" size="lg" title="식물 추가하기"/>}/>
          : <PlantList
            plantList={plantList}
            setPlantList={setPlantList}
            originPlantList={originPlantList}/>
      }
    </>
  )
}

export default Plant;
