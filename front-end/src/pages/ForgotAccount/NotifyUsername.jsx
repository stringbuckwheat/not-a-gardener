import {Link} from "react-router-dom";
import {Button} from "antd";
import React from "react";

const NotifyUsername = (props) => {
  const {email, memberList} = props;

  return (
    <div>
      <div className="mb-4">{email}로 가입한 아이디는 다음과 같습니다.</div>
      <div className="text-center">
        {
          memberList.map((username) => (
            <div className="mb-1"><b>* {username}</b></div>))
        }
      </div>
      <Link to="/login">
        <Button className="bg-orange text-white float-end mt-2">로그인 하러가기</Button>
      </Link>
    </div>
  )
}

export default NotifyUsername;
