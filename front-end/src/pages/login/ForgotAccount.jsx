import LoginPageWrapper from "./LoginPageWrapper";
import ForgotAccountCard from "../../components/card/ForgotAccountCard";
import {cilLockLocked, cilUser} from "@coreui/icons";
import React, {useState} from "react";
import NotifyUsername from "./forgot-account/NotifyUsername";
import SelectAccount from "./forgot-account/SelectAccount";
import ValidateGardener from "./forgot-account/ValidateGardener";

/**
 * ForgotAccount -> ValidateGardener -> VerifyAccountContent -> NotifyUsername
 *                                 -> SelectAccount -> ChangePassword
 * ValidateGardener에서 유저 인증 후, successContent로 넘긴 NotifyUsername이나 SelectAccount를 실행
 * @returns {JSX.Element} 아이디/비번찾기 고르기 페이지, 아이디 찾기 페이지, 비번 찾기 페이지
 */
const ForgotAccount = () => {
  const [forgot, setForgot] = useState("");
  const [email, setEmail] = useState("");
  const [gardenerList, setGardenerList] = useState([]);

  let props = {
    setEmail,
    setGardenerList
  }

  if (forgot === "username") {
    props = {
      ...props,
      icon: cilUser,
      title: "아이디 찾기",
      successContent: <NotifyUsername email={email} gardenerList={gardenerList}/>
    }
  } else if (forgot === "password") {
    props = {
      ...props,
      icon: cilLockLocked,
      title: "비밀번호 찾기",
      successContent: <SelectAccount email={email} gardenerList={gardenerList}/>
    }
  }

  return (
    <LoginPageWrapper>
      {
        forgot !== ""
          ? <ValidateGardener {...props}/>
          : (
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
      }
    </LoginPageWrapper>
  )
}

export default ForgotAccount;
