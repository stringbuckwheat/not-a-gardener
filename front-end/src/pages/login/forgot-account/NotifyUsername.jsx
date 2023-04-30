import {Link} from "react-router-dom";
import {Button, Space} from "antd";
import React from "react";

const NotifyUsername = ({email, gardenerList}) => {

  return (
    <div className="text-center">
      <div>{email}로 가입한 아이디는</div>
      <div className="mb-4">다음과 같습니다.</div>

      <div className="text-center mb-4">
        {
          gardenerList.map((username, index) => (
            <div className="mb-1" key={index}><b>* {username}</b></div>))
        }
      </div>

      <Space>
        <Link to="/login">
          <Button className="bg-orange text-white float-end mt-2">로그인 하러가기</Button>
        </Link>
      </Space>
    </div>
  )
}

export default NotifyUsername;
