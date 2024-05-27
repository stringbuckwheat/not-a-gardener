import {Col, Row} from "antd";
import React from "react";
import {ReactComponent as Logo} from "../../assets/images/logo.svg";
import Style from './LoginPageWrapper.module.scss'


const LoginPageWrapper = ({children}) => {
  return (
    <div className={Style.wrapper}>
      <Row className={Style.row}>
        <Col sm={22} md={9} style={{margin: "0 1rem"}}>
          <Row className={Style.logo}>
            <Logo className="float-end" width={"80vw"} height={"15vh"} fill={"#008000"}/>
          </Row>
          {children}
        </Col>
      </Row>
    </div>
  )
}

export default LoginPageWrapper
