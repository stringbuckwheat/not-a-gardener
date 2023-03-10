import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import {
  CButton,
  CCard,
  CCardBody,
  CCardGroup,
  CCol,
  CContainer,
  CForm,
  CFormInput,
  CInputGroup,
  CInputGroupText,
  CRow,
  CImage,
  CCardHeader
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import { cilLockLocked, cilUser } from '@coreui/icons'
import axios from 'axios'
import sprout from '../../assets/images/sprout.png'

function Login() {
  const navigate = useNavigate();

  const [login, setLogin] = useState({
    username: "",
    pw: ""
  })

  const [msg, setMsg] = useState('');

  const onChange = (e) => {
    const { name, value } = e.target;
    setLogin(setLogin => ({ ...login, [name]: value }))
  }

  // 입력 값 확인 및 submit
  const inputCheck = (e) => {
    e.preventDefault(); // reload 막기

    axios.post("/", login)
      .then((res) => {
        console.log("res", res);

        // local storage에 토큰과 기본 정보 저장
        localStorage.setItem("login", res.data.token);
        localStorage.setItem("memberNo", res.data.memberNo);
        localStorage.setItem("name", res.data.name);

        // garden 페이지로 이동
        navigate('/');
      })
      .catch((error) => {
        setMsg(error.response.data.errorDescription);
      });
  }

  return (
    <div className="bg-light min-vh-100 d-flex align-items-center">
      <CContainer fluid>
        <CRow className="justify-content-center">
          <CCol md={8}>
            <CCardGroup>
              <CCard className="p-4">
                <CCardHeader className="mb-0">
                  <h2>로그인</h2>
                </CCardHeader>
                <CCardBody>
                  <p className="text-medium-emphasis">{msg}</p>
                  <CForm onSubmit={inputCheck} method="POST">
                    <CInputGroup className="mb-3">
                      <CInputGroupText>
                        <CIcon icon={cilUser} />
                      </CInputGroupText>
                      <CFormInput placeholder="ID" name="username" onChange={onChange} />
                    </CInputGroup>
                    <CInputGroup className="mb-4">
                      <CInputGroupText>
                        <CIcon icon={cilLockLocked} />
                      </CInputGroupText>
                      <CFormInput
                        name="pw"
                        type="password"
                        placeholder="PW"
                        onChange={onChange}
                      />
                    </CInputGroup>
                    <CRow>
                      <CCol xs={4}>
                        <CButton type="submit" color="primary" className="px-4 align-self-start">로그인</CButton>
                      </CCol>
                      <CCol xs={4}></CCol>
                      <CCol xs={4}>
                        <CButton type="button" color="light" className="px-4 align-self-end">
                          계정 찾기
                        </CButton>
                      </CCol>
                    </CRow>
                  </CForm>
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
                </CCardBody>
              </CCard>
              <CCard className="text-white bg-primary py-5" sm={{ width: '100%' }} lg={{ width: '44%' }}>
                <CCardBody className="text-center">
                  <h2>not-a-gardener</h2>
                  <CImage fluid src={sprout} style={{ width: '50%' }} />
                  <p>
                    함께 키워요!
                  </p>
                  <Link to="/register">
                    <CButton type="button" color="primary" className="mt-3" active tabIndex={-1} >
                      가입하세요
                    </CButton>
                  </Link>
                </CCardBody>
              </CCard>
            </CCardGroup>
          </CCol>
        </CRow>
      </CContainer>
    </div>
  )
}

export default Login
