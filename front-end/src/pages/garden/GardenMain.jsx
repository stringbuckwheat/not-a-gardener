import GardenTodoList from "./GardenTodoList";
import React, {useEffect, useState} from "react";
import {CRow} from "@coreui/react";
import getChemicalListForSelect from "../../api/service/getChemicalListForSelect";
import GButton from "../../components/button/defaultButton/GButton";
import WaitingForWateringList from "./WaitingForWateringList";
import RoutineList from "./RoutineList";

const GardenMain = ({todoList, updateGardenAfterWatering, waitingList, routineList}) => {
  const onClickList = () => alert("구현중이에요");

  // 약품 리스트(물주기 시에 사용)
  const [chemicalList, setChemicalList] = useState([]);

  useEffect(() => {
    getChemicalListForSelect(setChemicalList);
  }, [])

  return (
    <div className="mt-3">
      <div>
        <h4 className="text-garden bold mb-2">가드너 <span className="bg-york">{localStorage.getItem("name")}</span>님의 오늘
          할 일이에요!</h4>
        <div className="mt-4">
          <CRow>
            <WaitingForWateringList waitingList={waitingList} />
            <RoutineList routineList={routineList} />
        </CRow>
      </div>
      <div className="d-flex justify-content-end">
        <GButton color="teal" className="float-end" onClick={onClickList}>리스트로 보기</GButton>
      </div>
      <div className="mt-2">
        <GardenTodoList
          updateGardenAfterWatering={updateGardenAfterWatering}
          chemicalList={chemicalList}
          todoList={todoList}/>
      </div>
    </div>
</div>
)
}

export default GardenMain
