import PlantTable from "./plantDetail/PlantTable";
import PlantListLayout from "src/components/data/layout/PlantListLayout";
import PlantListTag from "./PlantListTag";
import {useEffect, useState} from "react";
import AddPlant from "./AddPlant";
import getData from "../../api/backend-api/common/getData";
import {useDispatch} from "react-redux";

/**
 * 식물 리스트 메인 페이지
 * @param plantList
 * @param setPlantList
 * @param originPlantList
 * @param addPlant
 * @returns {JSX.Element} 식물 추가 페이지, 식물 리스트 페이지
 * @constructor
 */
const PlantList = () => {
  // const [searchWord, setSearchWord] = useState("");
  const [isAddFormOpened, setIsAddFormOpened] = useState(false);

  const dispatch = useDispatch();

  // const search = (searchWord) => {
  //   const searchedList = originPlantList.filter(plant => plant.plant.name.includes(searchWord));
  //   setPlantList(searchedList);
  // }

  const onMountPlantList = async () => {
    const rawPlace = await getData("/place");
    dispatch({type: "setPlaces", payload: rawPlace});
  }

  useEffect(() => {
    onMountPlantList();
  }, []);

  // useEffect(() => {
  //   if (searchWord !== "") {
  //     search(searchWord)
  //   } else {
  //     setPlantList(originPlantList);
  //   }
  // }, [searchWord])

  const switchAddForm = () => setIsAddFormOpened(!isAddFormOpened);

  return isAddFormOpened ? (
    <AddPlant
      afterAdd={switchAddForm}
    />
  ) : (
    <PlantListLayout
      title="나의 식물"
      url="/plant"
      deleteTitle="식물"
      // setSearchWord={setSearchWord}
      addFormOpen={switchAddForm}
      tags={<PlantListTag/>}
      bottomData={<PlantTable />}
    />
  )
}

export default PlantList;
