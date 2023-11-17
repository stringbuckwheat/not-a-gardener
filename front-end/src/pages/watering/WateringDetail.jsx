import GButton from "../../components/button/GButton";
import {useState} from "react";
import getChemicalListForSelect from "../../api/service/getChemicalListForSelect";
import getData from "../../api/backend-api/common/getData";
import WateringFormInCalendar from "./WateringFormInCalendar";
import WateringList from "./WateringList";

const WateringDetail = ({
                          selectedDate,
                          wateringDetail,
                          onAdd,
                          isWateringFormOpened,
                          setIsWateringFormOpened,
                          onDelete
                        }) => {
  const [chemicalList, setChemicalList] = useState([]);
  const [plantList, setPlantList] = useState([]);

  const onClickAddWatering = async () => {
    // plantList
    const originPlantList = await getData("/plant");
    let plantListForSelect = originPlantList;

    // 이미 물 준 식물 제외하기
    if (wateringDetail) {
      const validations = wateringDetail.map((watering) => watering.plantId);
      for (let i = 0; i < validations.length; i++) {
        plantListForSelect = plantListForSelect.filter((plant) => (plant.id !== validations[i]))
      }
    }

    plantListForSelect = plantListForSelect.map((plant) => ({label: plant.name, value: plant.id}));
    setPlantList(plantListForSelect);

    // chemicalList
    await getChemicalListForSelect(setChemicalList);
    setIsWateringFormOpened(true);
  }

  return (
    <div style={{marginTop: "2rem"}}>
      <div className="justify-content-between">
        <span
          style={{color: "#007BFF", marginBottom: "1rem", fontSize: "1rem"}}>{selectedDate?.format("M월 D일 dd요일")}</span>
        {
          !isWateringFormOpened
            ? <GButton color="teal" onClick={onClickAddWatering}>이 날짜에 물주기 추가하기</GButton>
            : <></>
        }
      </div>
      <WateringList wateringDetail={wateringDetail} onDelete={onDelete}/>
      {/* 물주기 폼 */}
      <WateringFormInCalendar
        selectedDate={selectedDate}
        onAdd={onAdd}
        chemicalList={chemicalList}
        plantList={plantList}
        isWateringFormOpened={isWateringFormOpened}
        setIsWateringFormOpened={setIsWateringFormOpened}
      />
    </div>
  )
}

export default WateringDetail
