import {
  CCard,
  CCardBody,
} from '@coreui/react'
import SocialLogin from "./SocialLogin";
import LoginForm from "./LoginForm";
import RegisterCard from "./RegisterCard";
import LoginPageWrapper from "./LoginPageWrapper";
import {Navigate} from "react-router-dom";
import React from "react";

const Login = () => {
  const sm = {width: '100%'}
  const lg = {width: '44%'}

  if (localStorage.getItem("accessToken")) {
    return <Navigate to="/" replace={true}/>
  }

  return (
    <LoginPageWrapper>
      <CCard className="p-4">
        <CCardBody >
          <LoginForm/>
          <SocialLogin/>
        </CCardBody>
      </CCard>
      <CCard
        className="text-garden bg-beige py-5 d-flex align-items-center"
        sm={sm}
        lg={lg}>
        <CCardBody className="text-center">
          <RegisterCard/>
        </CCardBody>
      </CCard>
    </LoginPageWrapper>
  )
}

export default Login
