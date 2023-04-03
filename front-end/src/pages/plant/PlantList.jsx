import PlantTable from "../../components/table/PlantTable";
import PlantListLayout from "src/components/data/layout/PlantListLayout";
import PlantListTag from "../../components/tag/PlantListTag";
import {useEffect, useState} from "react";
import AddPlant from "./AddPlant";
import getPlaceListForSelect from "../../api/service/getPlaceListForSelect";

const PlantList = ({plantList, setPlantList, originPlantList, addPlant}) => {
  const [placeList, setPlaceList] = useState([])
  const [searchWord, setSearchWord] = useState("");
  const [isAddFormOpened, setIsAddFormOpened] = useState(false);

  const search = (searchWord) => {
    const searchedList = originPlantList.filter(plant => plant.plant.plantName.includes(searchWord));
    setPlantList(searchedList);
  }

  useEffect(() => {
    if (searchWord !== "") {
      search(searchWord)
    } else {
      setPlantList(originPlantList);
    }
  }, [searchWord])

  const switchAddForm = () => setIsAddFormOpened(!isAddFormOpened);

  const addFormOpen = async () => {
    setPlaceList(await getPlaceListForSelect());
    switchAddForm();
  }

  return isAddFormOpened ? (
    <AddPlant
      placeList={placeList}
      addPlant={addPlant}
      closeAddForm={switchAddForm}
    />
  ) : (
    <PlantListLayout
      title="나의 식물"
      url="/plant"
      deleteTitle="식물"
      setSearchWord={setSearchWord}
      addFormOpen={addFormOpen}
      tags={<PlantListTag howManyPlants={plantList.length}/>}
      bottomData={<PlantTable
        originPlantList={plantList}
        setPlantList={setPlantList}
        placeList={placeList}/>}
    />
  )
}

export default PlantList;
