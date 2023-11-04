import PlantTable from "./plantDetail/PlantTable";
import PlantListTag from "./PlantListTag";
import {useEffect, useState} from "react";
import AddPlant from "./AddPlant";
import getData from "../../api/backend-api/common/getData";
import {useDispatch} from "react-redux";
import {CButton, CCard, CCardBody, CCol} from "@coreui/react";
import {Space} from "antd";

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
    <div className="row justify-content-md-center">
      <CCol md="auto" className="minWidth-full">
        <CCard sm={6} className="mb-4">
          <CCardBody>
            <div>
              <h4 className="mt-3 mb-3">{"나의 식물"}</h4>
              <PlantListTag/>
            </div>
            <div className="float-end mb-3">
              <Space>
                {/*<Search setSearchWord={setSearchWord}/>*/}
                <CButton
                  onClick={switchAddForm}
                  color="success"
                  size="sm"
                  variant="outline"
                  shape="rounded-pill">
                  식물 추가하기
                </CButton>
              </Space>
            </div>
            <PlantTable />
          </CCardBody>
        </CCard>
      </CCol>
    </div>
  )
}

export default PlantList;
