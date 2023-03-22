import PlantTable from "../../components/table/PlantTable";
import PlantListLayout from "src/components/data/layout/PlantListLayout";
import PlantListTag from "../../components/tag/PlantListTag";
import {useEffect, useState} from "react";
import getData from "../../api/backend-api/common/getData";
import onMount from "../../api/service/onMount";

const PlantList = (props) => {
  const plantList = props.plantList;
  const setPlantList = props.setPlantList;
  const originPlantList = props.originPlantList;

  const [ placeList, setPlaceList] = useState([])
  const [ searchWord, setSearchWord] = useState("");

  useEffect(() => {
    onMount("/place", setPlaceList);
  }, []);


  const search = (searchWord) => {
    const searchedList = originPlantList.filter(plant => plant.plant.plantName.includes(searchWord));
    setPlantList(searchedList);
  }

  useEffect(() => {
    console.log("현재 검색어: ", searchWord);

    if (searchWord !== "") {
      search(searchWord)
    } else {
      setPlantList(originPlantList);
    }
  }, [searchWord])

  return (
    <PlantListLayout
      title="나의 식물"
      url="/plant"
      deleteTitle="식물"
      setSearchWord={setSearchWord}
      tags={<PlantListTag howManyPlants={plantList.length}/>}
      bottomData={<PlantTable
        plantList={plantList}
        setPlantList={setPlantList}
        placeList={placeList}/>}
    />
  )
}

export default PlantList;
