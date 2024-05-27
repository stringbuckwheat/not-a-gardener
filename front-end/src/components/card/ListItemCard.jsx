import {Link} from "react-router-dom";
import {Card, Col, Row} from "antd";
import React from "react";
import Style from './ItemCard.module.scss'

/**
 * 비료/살충/살균제 카드
 * @param chemical
 * @returns {JSX.Element}
 * @constructor
 */
const ListItemCard = ({color, icon, link, state, name, type, detail}) => {
  return (
    <Col xs={24} sm={12} md={8} lg={6} className={Style.wrapper}>
      <Link
        to={link}
        state={state}
        className="no-text-decoration">
        <Card className={Style.card}>
          <Row>
            <Col>{icon}</Col>
            <Col className={Style.info}>
              <div style={{color: color}} className={Style.name}>{name}</div>
              <div className={Style.type}>{type}</div>
              <div className={Style.detail}>{detail}</div>
            </Col>
          </Row>
        </Card>
      </Link>
    </Col>
  )
}

export default ListItemCard;
