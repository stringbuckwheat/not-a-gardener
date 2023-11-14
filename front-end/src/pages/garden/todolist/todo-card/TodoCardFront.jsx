import WateringCodeIcon from "../../../../components/etc/WateringCodeIcon";
import TodoTag from "../TodoTag";
import getWateringMsg from "../../../../utils/function/getWateringMsg";
import React from "react";
import {Col, Row} from "antd";
import Style from './TodoCard.moduls.scss'

const TodoCardFront = ({garden, color, flipCard}) => {
  return (
    <Row onClick={() => flipCard(garden.plant.id)}>
    <Row className="d-flex align-items-center" >
      <Col xs={5} className="text-center mr-1">
        <WateringCodeIcon wateringCode={garden.gardenDetail.wateringCode}/>
      </Col>
      <Col xs={16}>
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
      </Col>
    </Row>
    </Row>
  )
}

export default TodoCardFront
