import WateringCodeIcon from "../../../../components/etc/WateringCodeIcon";
import TodoTag from "../TodoTag";
import getWateringMsg from "../../../../utils/function/getWateringMsg";
import React from "react";
import {Col, Row} from "antd";
import Style from './TodoCard.module.scss'

const TodoCardFront = ({garden, flipCard, color}) => {
  return (
    <Row onClick={() => flipCard(garden.plant.id)} className={Style.frontWrap}>
      <Row className={Style.wrap}>
        <Col xs={6} className={Style.center}>
          <WateringCodeIcon wateringCode={garden.gardenDetail.wateringCode} size={"3.5rem"}/>
        </Col>
        <Col xs={16}>
          <div>
            <div className={Style.name}>{garden.plant.name}</div>
            <div className="new-line">
              <small>{garden.plant.species}</small></div>
            <TodoTag plant={garden}/>
            <p
              style={{color: color}}
              className={Style.msg}>
              {getWateringMsg(garden.gardenDetail)}
            </p>
          </div>
        </Col>
      </Row>
    </Row>
  )
}

export default TodoCardFront
