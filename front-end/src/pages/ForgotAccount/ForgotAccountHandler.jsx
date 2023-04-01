import ForgotAccountCard from "./ForgotAccountCard";
import {cilLockLocked, cilUser} from "@coreui/icons";
import React, {useState} from "react";
import {CCol} from "@coreui/react";
import VerifyAccount from "./VerifyAccount";
import {Link} from "react-router-dom";
import {Button} from "antd";
import ForgotCardWrapper from "./ForgotCardWrapper";

const ForgotAccountHandler = () => {
  const [forgot, setForgot] = useState("");

  const [email, setEmail] = useState("");
  const [memberList, setMemberList] = useState([]);

  console.log("email", email);
  console.log("memberList,", memberList);

  if (forgot === "") {
    return (
      <>
        <ForgotAccountCard
          onClick={() => setForgot("username")}
          color="orange"
          icon={cilUser}
          title="아이디를 잊어버렸어요"/>
        <ForgotAccountCard
          onClick={() => setForgot("password")}
          color="beige"
          icon={cilLockLocked}
          title="비밀번호를 잊어버렸어요"/>
      </>
    )
  } else if (forgot === "username") {
    return (
      <>
        <CCol md={3}>
          <ForgotAccountCard
            color="orange"
            icon={cilUser}
            iconSize="6xl"
            buttonSize="sm"
            title="아이디 찾기"/>
        </CCol>
        <VerifyAccount setEmail={setEmail} setMemberList={setMemberList}>
          <ForgotCardWrapper>
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
          </ForgotCardWrapper>
        </VerifyAccount>
      </>
    )
  } else {
    return (
      <>
        <CCol md={3}>
          <ForgotAccountCard
            color="orange"
            icon={cilLockLocked}
            iconSize="6xl"
            buttonSize="sm"
            title="비밀번호 찾기"/>
        </CCol>
        <VerifyAccount />
      </>
    )
  }
}

export default ForgotAccountHandler;
