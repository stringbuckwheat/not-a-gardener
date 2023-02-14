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
import sprout from '../assets/images/sprout.png'

function Login(){
  const [login, setLogin] = useState({
    username: "",
    pw: ""
  })
  const [msg, setMsg] = useState('');

  const onChange = (e) => {
    const {name, value} = e.target;
    setLogin(setLogin => ({...login, [name]: value}))
  }

  const navigate = useNavigate();

  // 입력 값 확인 및 submit
  const inputCheck = (e) => {
    e.preventDefault(); // reload 막기

      axios.post("/", login)
      .then((res) => {
        // local storage에 토큰 저장
        localStorage.setItem("login", res.data);

        // garden 페이지로 이동
        navigate('/garden');
      })
      .catch((error) => {
         setMsg(error.response.data.message);
      });

  }

  const onGoogleLogin = () => {
    axios.get("/oauth2/authorization/google")
    .then((res) => {
      console.log(res);
    })
  }

  return (
    <div className="bg-light min-vh-100 d-flex flex-row align-items-center">
      <CContainer>
        <CRow className="justify-content-center">
          <CCol md={8}>
            <CCardGroup>
              <CCard className="p-4">
                <CCardHeader className="mb-5">
                  <h1>로그인</h1>
                  <p className="text-medium-emphasis">{msg}</p>
                </CCardHeader>
                <CCardBody>
                  <CForm onSubmit={inputCheck}>
                    <CInputGroup className="mb-3">
                      <CInputGroupText>
                        <CIcon icon={cilUser} />
                      </CInputGroupText>
                      <CFormInput placeholder="ID" name="username" onChange={onChange}/>
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
                    <div className="mt-1">
                    <CRow>
                      <CCol xs={4}>
                        <CButton type="submit" color="primary" className="px-4 align-self-start">로그인</CButton>
                      </CCol>
                      <CCol xs={4}></CCol>
                      <CCol xs={4}>
                        <CButton color="link" className="px-0 align-self-end">
                          아이디/비밀번호 찾기
                        </CButton>
                      </CCol>
                    </CRow>
                    </div>
                  </CForm>
                  <CRow>
                      <CCol xs={4}>
                        <a href="http://localhost:8080/oauth2/authorization/google">
                          <CButton color="link" className="px-0 align-self-end">구글 로그인</CButton>
                        </a>
                      </CCol>
                    </CRow>
                </CCardBody>
              </CCard>
              <CCard className="text-white bg-primary py-5" sm={{width: '100%'}} lg={{width: '44%'}}>
                <CCardBody className="text-center">
                  <div>
                    <h2>식물 키우기!</h2>
                    <CImage fluid src={sprout} style={{width: '50%'}} />
                    <p>
                      가드너가 되지 못한 당신을 위한 해결 방안
                    </p>
                    <Link to="/register">
                      <CButton color="primary" className="mt-3" active tabIndex={-1} >
                        가입하세요
                      </CButton>
                    </Link>
                  </div>
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
