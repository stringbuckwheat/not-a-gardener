import {Button, Tag} from "antd";
import GardenTodoList from "./GardenTodoList";
import React, {useEffect, useState} from "react";
import {CCol, CRow} from "@coreui/react";
import getChemicalListForSelect from "../../api/service/getChemicalListForSelect";
import {Link} from "react-router-dom";

const GardenMain = ({todoList, updateGardenAfterWatering, waitingList}) => {
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
              <div className="small fw-bold">물주기 정보를 기다리고 있는 식물들</div>
              <div>
                {
                  waitingList.map((plant) => (
                    <Link to={`/plant/${plant.plantNo}`}>
                      <Tag color="green">{plant.plantName}</Tag>
                    </Link>
                  ))
                }
              </div>
            </CCol>
            <CCol md={6} xs={12}>
              <div className="small fw-bold">{localStorage.getItem("name")}님의 루틴</div>
              <div>
                <Tag color="geekblue">팍스</Tag>
                <Tag color="geekblue">아디안텀</Tag>
                <Tag color="geekblue">글로리오섬들</Tag>
              </div>
            </CCol>
          </CRow>
        </div>
        <div className="d-flex justify-content-end">
          <Button className="bg-teal text-white float-end" onClick={onClickList}>리스트로 보기</Button>
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
