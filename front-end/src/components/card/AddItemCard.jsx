import {Card, Col, Row} from "antd";
import React from "react";
import {PlusOutlined} from "@ant-design/icons";
import Style from './ItemCard.module.scss'

/**
 * 추가하기 카드
 * @param addMsg ex. 식물 추가하기
 * @param onClick
 * @returns {JSX.Element}
 * @constructor
 */
const AddItemCard = ({addMsg, onClick}) => {
  return (
    <Col md={6} xs={24} className={Style.wrapper}>
      <Card className={Style.card} onClick={onClick}>
        <Row>
          <Col style={{alignItems: "center"}}>
              <PlusOutlined className={Style.icon} />
          </Col>
          <Col style={{marginLeft: "1.5rem",}}>
            <div style={{color: "#4f5d73", fontWeight: "bold", fontSize: "1rem", marginTop: "1.5rem"}}>{addMsg}</div>
          </Col>
        </Row>
      </Card>
    </Col>
  )
}

export default AddItemCard
