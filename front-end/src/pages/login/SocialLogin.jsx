import {CRow} from "@coreui/react";
import React from "react";
import SocialLoginButton from "../../components/button/SocialLoginButton";

const SocialLogin = () => {
  const recentLogin = localStorage.getItem("provider");
  const providers = ["kakao", "google", "naver"];

  return (
    <CRow className='mt-5'>
      <h6>간편 로그인</h6>
      <hr/>
      {
        providers.map((provider, index) => <SocialLoginButton provider={provider} recentLogin={recentLogin} key={index}/>)
      }
    </CRow>
  )
}

export default SocialLogin;
