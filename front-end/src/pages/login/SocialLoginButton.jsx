import {CCol, CRow} from "@coreui/react";
import React from "react";

const SocialLoginButton = () => {
  return (
    <CRow className='mt-5'>
      <h6>간편 로그인</h6>
      <hr />
      <CCol xs={4}>
        <a href="http://localhost:8080/oauth2/authorization/kakao" class="social-button" id="kakao-connect"></a>
      </CCol>
      <CCol xs={4}>
        <a href="http://localhost:8080/oauth2/authorization/google" class="social-button" id="google-connect"></a>
      </CCol>
      <CCol xs={4}>
        <a href="http://localhost:8080/oauth2/authorization/naver" class="social-button" id="naver-connect"></a>
      </CCol>
    </CRow>
  )
}

export default SocialLoginButton;
