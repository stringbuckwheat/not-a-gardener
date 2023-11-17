import LoginPageWrapper from "./LoginPageWrapper";
import ForgotAccountCard from "../../components/card/ForgotAccountCard";
import React, {useState} from "react";
import NotifyUsername from "./forgot-account/NotifyUsername";
import SelectAccount from "./forgot-account/SelectAccount";
import ValidateGardener from "./forgot-account/ValidateGardener";
import {Navigate} from "react-router-dom";
import {LockOutlined, UserOutlined} from "@ant-design/icons";
import {Row} from "antd";

/**
 * 아이디를 찾을지, 비밀번호를 찾을지 선택하는 페이지
 * ForgotAccount -> ValidateGardener -> VerifyAccountContent -> NotifyUsername
 *                                 -> SelectAccount -> ChangePassword
 * ValidateGardener에서 유저 인증 후, successContent로 넘긴 NotifyUsername이나 SelectAccount를 실행
 * @returns {JSX.Element} 아이디/비번찾기 고르기 페이지, 아이디 찾기 페이지, 비번 찾기 페이지
 */
const ForgotAccount = () => {
  if (localStorage.getItem("accessToken")) {
    return <Navigate to="/" replace={true}/>
  }

  // 찾을 회원정보 저장(username, password)
  const [forgot, setForgot] = useState("");
  // 유저가 입력한 이메일
  const [email, setEmail] = useState("");
  // 해당 이메일로 가입한 아이디 목록
  const [gardenerList, setGardenerList] = useState([]);

  // 공통 props
  let props = {
    setEmail,
    setGardenerList
  }

  // 아이디 혹은 비밀번호 찾기를 눌렀을 시 띄울 컴포넌트와 이에 필요한 props들
  if (forgot === "username") {
    props = {
      ...props,
      icon: <UserOutlined style={{fontSize: "2rem"}}/>,
      title: "아이디 찾기",
      successContent: <NotifyUsername email={email} gardenerList={gardenerList}/>
    }
  } else if (forgot === "password") {
    props = {
      ...props,
      icon: <LockOutlined style={{fontSize: "2rem"}}/>,
      title: "비밀번호 찾기",
      successContent: <SelectAccount email={email} gardenerList={gardenerList}/>
    }
  }

  // forgot의 값에 따라 다른 컴포넌트 렌더링
  return (
    <LoginPageWrapper>
      {
        forgot !== ""
          ? <ValidateGardener {...props}/>
          : (
            <Row style={{justifyContent: "center"}}>
              <ForgotAccountCard
                onClick={() => setForgot("username")}
                icon={<UserOutlined style={{fontSize: "7rem"}}/>}
                title="아이디를 잊어버렸어요"/>
              <ForgotAccountCard
                onClick={() => setForgot("password")}
                icon={<LockOutlined style={{fontSize: "7rem"}}/>}
                title="비밀번호를 잊어버렸어요"/>
            </Row>
          )
      }
    </LoginPageWrapper>
  )
}

export default ForgotAccount;
