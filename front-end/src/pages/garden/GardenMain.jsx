import {Tag} from "antd";
import GardenTodoList from "./GardenTodoList";
import React, {useEffect, useState} from "react";
import {CCol, CRow} from "@coreui/react";
import getChemicalListForSelect from "../../api/service/getChemicalListForSelect";
import {Link} from "react-router-dom";
import GButton from "../../components/button/defaultButton/GButton";

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
            <CCol md={6} xs={12}>
              <div className="small fw-bold text-black">물주기 정보를 기다리고 있는 식물들</div>
              <div>
                {
                  waitingList.map((plant, idx) => (
                    <Link to={`/plant/${plant.plantNo}`} key={idx}>
                      <Tag color="green">{plant.plantName}</Tag>
                    </Link>
                  ))
                }
              </div>
            </CCol>
            <CCol md={6} xs={12}>
              <Link to="/schedule" className="no-text-decoration text-black">
                <div className="small fw-bold">{localStorage.getItem("name")}님의 루틴</div>
              </Link>
              <div>
                {
                  routineList.map((routine) =>
                    <Tag color={routine.isCompleted === "Y" ? "" : "geekblue"}>{routine.routineContent}</Tag>)
                }
              </div>
          </CCol>
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
