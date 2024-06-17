import {Card, Col, Row} from "antd";
import React from "react";
import {PlusOutlined, PlusSquareFilled} from "@ant-design/icons";
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
    <Col xs={24} sm={12} md={8} lg={6} className={Style.wrapper} style={{marginBottom: "1rem"}}>
      <Card onClick={onClick} className={Style.card} bodyStyle={{width: "100%", padding: 0}}>
        <Row align="middle" className={Style.row}>
          <Col xs={8} className={Style.iconCol}>
            <div className={Style.iconContainer}>
              <PlusSquareFilled style={{color: "darkgray"}} className={Style.icon}/>
            </div>
          </Col>
          <Col xs={16} className={Style.textCol}>
            <div style={{
              color: "#4f5d73",
              fontWeight: "bold",
              fontSize: "1rem",
            }}>{addMsg}</div>
          </Col>
        </Row>
      </Card>
    </Col>
  )
}

export default AddItemCard
