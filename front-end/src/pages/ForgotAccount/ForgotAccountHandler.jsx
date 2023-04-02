import ForgotAccountCard from "./ForgotAccountCard";
import {cilLockLocked, cilUser} from "@coreui/icons";
import React, {useState} from "react";
import {CCol} from "@coreui/react";
import VerifyAccount from "./VerifyAccount";
import NotifyUsername from "./NotifyUsername";
import SelectAccount from "./SelectAccount";
import ForgotCardWrapper from "./ForgotCardWrapper";

const ForgotAccountHandler = () => {
  const [forgot, setForgot] = useState("");

  const [email, setEmail] = useState("");
  const [memberList, setMemberList] = useState([]);

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
        <ForgotCardWrapper>
          <VerifyAccount
            setEmail={setEmail}
            setMemberList={setMemberList}
            successContent={<NotifyUsername email={email} memberList={memberList}/>}/>
        </ForgotCardWrapper>
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
        <ForgotCardWrapper>
          <VerifyAccount
            setEmail={setEmail}
            setMemberList={setMemberList}
            successContent={<SelectAccount email={email} memberList={memberList}/>}
          />
        </ForgotCardWrapper>
      </>
    )
  }
}

export default ForgotAccountHandler;
