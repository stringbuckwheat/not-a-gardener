import {useEffect, useState} from "react";
import AddPlantButton from "src/components/button/AddPlantButton";
import NoItem from "src/components/NoItem";
import PlantList from "./PlantList";
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";

const Plant = () => {
  const [isLoading, setLoading] = useState(true);
  const [hasPlant, setHasPlant] = useState(false);
  const [originPlantList, setOriginPlantList] = useState([])
  const [plantList, setPlantList] = useState([]);

  const onMountPlant = async () => {
    const data = await getData("/garden");
    setLoading(false);
    setHasPlant(data.length > 0);
    setPlantList(data);
    setOriginPlantList(data);
  }

  useEffect(() => {
    onMountPlant();
  }, [])

  if (isLoading) {
    return <Loading/>
  } else if (!hasPlant) {
    return <NoItem
      title="등록된 식물이 없어요"
      button={<AddPlantButton addUrl="/plant/add" size="lg"/>}/>
  } else {
    return <PlantList
      plantList={plantList}
      setPlantList={setPlantList}
      originPlantList={originPlantList}/>
  }
}

export default Plant;
