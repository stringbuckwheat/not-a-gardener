import {useEffect, useState} from "react";
import AddPlantButton from "src/components/button/AddPlantButton";
import NoItem from "src/components/empty/NoItem";
import PlantList from "./PlantList";
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";
import getPlaceListForSelect from "../../api/service/getPlaceListForSelect";
import NoItemForPlant from "../../components/empty/NoItemForPlant";

const Plant = () => {
  const [isLoading, setLoading] = useState(true);
  const [hasPlant, setHasPlant] = useState(false);
  const [originPlantList, setOriginPlantList] = useState([])
  const [plantList, setPlantList] = useState([]);

  const onMountPlant = async () => {
    const data = await getData("/garden/plants");
    setLoading(false);

    setHasPlant(data.length > 0);

    data.sort((a, b) => new Date(b.plant.createDate) - new Date(a.plant.createDate));

    setPlantList(data);
    setOriginPlantList(data);
  }

  useEffect(() => {
    onMountPlant();
  }, [])

  const addPlant = (plant) => {
    plantList.unshift(plant);

    setPlantList(plantList);
    setOriginPlantList(plantList);
  }


  if (isLoading) {
    return <Loading/>
  } else if (!hasPlant) {
    return <NoItemForPlant
      addPlant={addPlant}
      afterAdd={() => setHasPlant(true)}
    />
  } else {
    return <PlantList
      plantList={plantList}
      setPlantList={setPlantList}
      addPlant={addPlant}
      originPlantList={originPlantList}/>
  }
}

export default Plant;
