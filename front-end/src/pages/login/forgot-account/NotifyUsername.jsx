import {Link} from "react-router-dom";
import {Button, Space} from "antd";
import React from "react";

const NotifyUsername = ({email, gardenerList}) => {

  return (
    <div className="text-center">
      <div>{email}로 가입한 아이디는</div>
      <div style={{marginBottom: "1.5rem"}}>다음과 같습니다.</div>

      <div className="text-center" style={{marginBottom: "1.5rem"}}>
        {
          gardenerList.map((gardener, index) => (
            <div style={{marginBottom: "0.25rem"}} key={index}><b>* {gardener.username}</b></div>))
        }
      </div>
      <Space>
        <Link to="/login">
          <Button type="primary" className="float-end" style={{marginTop: "0.5rem"}}>로그인 하러가기</Button>
        </Link>
      </Space>
    </div>
  )
}

export default NotifyUsername;
