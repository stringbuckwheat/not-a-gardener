import SocialLogin from "./SocialLogin";
import LoginForm from "./LoginForm";
import {Link, Navigate} from "react-router-dom";
import React from "react";
import {UnlockOutlined, UserAddOutlined} from "@ant-design/icons";
import LoginPageWrapper from "./LoginPageWrapper";

const Login = () => {
  if (localStorage.getItem("accessToken")) {
    return <Navigate to="/" replace={true}/>
  }

  return (
    <LoginPageWrapper>
      <LoginForm/>
      <SocialLogin/>
      <div style={{marginTop: "1.5rem", textAlign: "center"}}>
        <Link to="/forgot" className="link" style={{margin: "1rem"}}>
          <span><UnlockOutlined/> 아이디/비밀번호 찾기</span>
        </Link>
        <Link to="/register" className="link">
          <span><UserAddOutlined/> 회원가입</span>
        </Link>
      </div>
    </LoginPageWrapper>
  )
}

export default Login
