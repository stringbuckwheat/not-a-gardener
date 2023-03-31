import LoginPageWrapper from "./LoginPageWrapper";
import React, {useState} from "react";
import ForgotAccountHandler from "./ForgotAccountHandler";

const ForgotAccount = () => {
  const [forgot, setForgot] = useState("");

  return (
    <LoginPageWrapper>
      <ForgotAccountHandler forgot={forgot} setForgot={setForgot} />
    </LoginPageWrapper>
  )
}

export default ForgotAccount;
