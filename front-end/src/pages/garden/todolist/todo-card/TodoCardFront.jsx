import {CCol, CRow} from "@coreui/react";
import WateringCodeIcon from "../../../../components/etc/WateringCodeIcon";
import TodoTag from "../TodoTag";
import getWateringMsg from "../../../../utils/function/getWateringMsg";
import React from "react";

const TodoCardFront = ({garden, color, flipCard}) => {
  return (
    <div onClick={() => flipCard(garden.plant.id)}>
    <CRow className="d-flex align-items-center" >
      <CCol xs={2} className="text-center">
        <WateringCodeIcon wateringCode={garden.gardenDetail.wateringCode}/>
      </CCol>
      <CCol xs={1}></CCol>
      <CCol xs={8}>
        <div>
          <div className={`fs-6 fw-semibold text-black`}>{garden.plant.name}</div>
          <div className="small text-black new-line">
            <small>{garden.plant.species}</small></div>
          <TodoTag className="small" plant={garden}/>
          <p
            className={`text-${color} fw-semibold small new-line mt-2 `}>
            {getWateringMsg(garden.gardenDetail)}
          </p>
        </div>
      </CCol>
    </CRow>
    </div>
  )
}

export default TodoCardFront
