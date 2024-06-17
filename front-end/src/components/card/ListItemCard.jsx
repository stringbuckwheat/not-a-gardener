import {Link} from "react-router-dom";
import {Card, Col, Row} from "antd";
import React from "react";
import Style from './ItemCard.module.scss';

/**
 * 비료/살충/살균제 카드
 * @param chemical
 * @returns {JSX.Element}
 * @constructor
 */
const ListItemCard = ({color, icon, link, state, name, type, detail}) => {
  return (
    <Link to={link} state={state} className="no-text-decoration">
      <Card className={Style.card} bodyStyle={{width: "100%", padding: 0}}>
        <Row align="middle" className={Style.row}>
          <Col xs={8} className={Style.iconCol}>
            <div style={{color}} className={Style.iconContainer}>
              {React.cloneElement(icon, {className: `${icon.props.className} ${Style.icon}`})}
            </div>
          </Col>
          <Col xs={16} className={Style.textCol}>
            <div style={{color}} className={Style.name}>{name}</div>
            <div className={Style.type}>{type}</div>
            <div className={Style.detail}>{detail}</div>
          </Col>
        </Row>
      </Card>
    </Link>
  );
}

export default ListItemCard;
