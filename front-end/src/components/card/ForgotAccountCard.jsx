import React from "react";
import {Card, Col} from "antd";
import Style from '../../pages/login/ForgotAccount.module.scss'

/**
 * 계정 찾기 카드
 * @param icon
 * @param color
 * @param onClick
 * @param title
 * @param buttonSize
 * @param iconSize
 * @returns {JSX.Element}
 * @constructor
 */
const ForgotAccountCard = ({icon, onClick, title}) => {
  return (
    <Col md={12} style={{padding: "0.5rem"}} >
      <Card
        className={Style.card}
        style={{minHeight: "40vh", alignItems: "center", justifyContent: "center",}}
        onClick={onClick}>
        <div className="text-center">
          <div>
            {icon}
          </div>
          <div style={{fontSize: "1rem"}}>{title}</div>
        </div>
      </Card>
    </Col>
  )
}

export default ForgotAccountCard;
