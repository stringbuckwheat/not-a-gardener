import ForgotAccountCard from "./ForgotAccountCard";
import {cilLockLocked, cilUser} from "@coreui/icons";
import React from "react";

const ForgotAccountHandler = (props) => {
  const {forgot, setForgot} = props;

  if(forgot === ""){
    return (
      <>
        <ForgotAccountCard type="username" setForgot={setForgot} color="orange" icon={cilUser}/>
        <ForgotAccountCard type="password" setForgot={setForgot} color="beige" icon={cilLockLocked}/>
      </>
    )
  } else if(forgot === "username"){
    return (
      <>
        <div>아이디 찾기</div>
      </>
    )
  } else {
    return (
      <>
        <div>비번 찾기</div>
      </>
    )
  }
}

export default ForgotAccountHandler;
