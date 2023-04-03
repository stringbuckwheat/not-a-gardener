import LoginPageWrapper from "./LoginPageWrapper";
import ForgotAccountCard from "../../components/card/ForgotAccountCard";
import {cilLockLocked, cilUser} from "@coreui/icons";
import React, {useState} from "react";
import NotifyUsername from "./forgot-account/NotifyUsername";
import SelectAccount from "./forgot-account/SelectAccount";
import ValidateMember from "./forgot-account/ValidateMember";

/**
 * ForgotAccount -> ValidateMember -> NotifyUsername
 *                                 -> SelectAccount -> ChangePassword
 * @returns {JSX.Element} 아이디/비번찾기 고르기 페이지, 아이디 찾기 페이지, 비번 찾기 페이지
 */
const ForgotAccount = () => {
  const [forgot, setForgot] = useState("");
  const [email, setEmail] = useState("");
  const [memberList, setMemberList] = useState([]);

  let props = {
    setEmail: setEmail,
    setMemberList: setMemberList
  }

  if (forgot === "username") {
    props = {
      ...props,
      icon: cilUser,
      title: "아이디 찾기",
      successContent: <NotifyUsername email={email} memberList={memberList} />
    }
  } else if (forgot === "password") {
    props = {
      ...props,
      icon: cilLockLocked,
      title: "비밀번호 찾기",
      successContent: <SelectAccount email={email} memberList={memberList}/>
    }
  }

  return (
    <LoginPageWrapper>
      {
        forgot !== ""
          ? <ValidateMember {...props}/>
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
