import {CCol} from "@coreui/react";
import ForgotAccountCard from "../../../components/card/ForgotAccountCard";
import ForgotCardWrapper from "../../../components/card/wrapper/ForgotCardWrapper";
import VerifyAccountContent from "./VerifyAccountContent";
import React, {useState} from "react";

const ValidateMember = ({icon, title, setEmail, setMemberList, successContent}) => {
  return (
    <>
      <CCol md={3}>
        <ForgotAccountCard
          color="orange"
          icon={icon}
          iconSize="6xl"
          buttonSize="sm"
          title={title}/>
      </CCol>
      <ForgotCardWrapper>
        <VerifyAccountContent
          setEmail={setEmail}
          setMemberList={setMemberList}
          successContent={successContent}/>
      </ForgotCardWrapper>
    </>
  )
}

export default ValidateMember;
