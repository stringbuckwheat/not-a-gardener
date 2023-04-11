import {CContainer} from "@coreui/react";
import GButton from "../../components/button/defaultButton/GButton";
import {useState} from "react";
import getChemicalListForSelect from "../../api/service/getChemicalListForSelect";
import getData from "../../api/backend-api/common/getData";
import WateringFormInCalendar from "./WateringFormInCalendar";
import WateringList from "./WateringList";

const WateringDetail = ({selectedDate, wateringDetail, onAdd, isWateringFormOpened, setIsWateringFormOpened}) => {
  console.log({wateringDetail})
  const [chemicalList, setChemicalList] = useState([]);
  const [plantList, setPlantList] = useState([]);

  const onClickAddWatering = async () => {
    // plantList
    const originPlantList = await getData("/plant");
    let plantListForSelect = originPlantList;

    // 이미 물 준 식물 제외하기
    if (wateringDetail) {
      const validations = wateringDetail.map((watering) => watering.plantNo);
      for (let i = 0; i < validations.length; i++) {
        plantListForSelect = plantListForSelect.filter((plant) => (plant.plantNo !== validations[i]))
      }
    }

    plantListForSelect = plantListForSelect.map((plant) => ({label: plant.plantName, value: plant.plantNo}));
    setPlantList(plantListForSelect);

    // chemicalList
    await getChemicalListForSelect(setChemicalList);
    setIsWateringFormOpened(true);
  }

  return (
    <CContainer className="mt-4">
      <div className="d-flex justify-content-between">
        <span className="fw-6 text-info bold">{selectedDate?.format("M월 D일 dd요일")}</span>
        {
          !isWateringFormOpened
            ? <GButton color="teal" onClick={onClickAddWatering}>이 날짜에 물주기 추가하기</GButton>
            : <></>
        }
      </div>
      <div className="mb-2">
        <WateringList wateringDetail={wateringDetail}/>
      </div>
      {/* 물주기 폼 */}
      <div className="mb-2">
        <WateringFormInCalendar
          onAdd={onAdd}
          chemicalList={chemicalList}
          plantList={plantList}
          isWateringFormOpened={isWateringFormOpened}
          setIsWateringFormOpened={setIsWateringFormOpened}
        />
      </div>
    </CContainer>
  )
}

export default WateringDetail
