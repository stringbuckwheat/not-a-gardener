import React, { useState } from 'react'
import { Link } from 'react-router-dom'
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
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import { cilLockLocked, cilUser } from '@coreui/icons'
import axios from 'axios'

function Login(){
  const [id, setId] = useState("");
  const [pw, setPw] = useState("");

  // 입력 값 확인 및 submit
  const inputCheck = (e) => {
    e.preventDefault(); // reload 막기

    if(!id){
      return alert("ID를 입력하세요");
    } else if(!pw){
      return alert("PW를 입력하세요");
    } else {
      console.log("id: " + id);
      console.log(typeof id)
      console.log("pw: " + pw);
      console.log(typeof pw)

      // object의 key, value 이름이 같으면 생략 가능
      const data = {id, pw};

      axios.post("/", data)
      .then((res) => {
        console.log(res);

        /*
        if(res.data.code === 200) {
          console.log("로그인");
          dispatch(loginUser(res.data.userInfo));
          setMsg("");
        }

        if(res.data.code === 400) {
          setMsg("아이디와 비밀번호를 입력해주세요.");
        }
        
        if(res.data.code === 401) {
          setMsg("존재하지 않는 ID입니다.");
        }
        
        if(res.data.code === 402) {
          setMsg("비밀번호가 틀립니다.");
        }
        */
      });
    }
  }

  return (
    <div className="bg-light min-vh-100 d-flex flex-row align-items-center">
      <CContainer>
        <CRow className="justify-content-center">
          <CCol md={8}>
            <CCardGroup>
              <CCard className="p-4">
                <CCardBody>
                  <CForm onSubmit={inputCheck}>
                    <h1>로그인</h1>
                    <p className="text-medium-emphasis"></p>
                    <CInputGroup className="mb-3">
                      <CInputGroupText>
                        <CIcon icon={cilUser} />
                      </CInputGroupText>
                      <CFormInput placeholder="ID" name="id" onChange={(e) => setId(e.target.value)}/>
                    </CInputGroup>
                    <CInputGroup className="mb-4">
                      <CInputGroupText>
                        <CIcon icon={cilLockLocked} />
                      </CInputGroupText>
                      <CFormInput
                        name="pw"
                        type="password"
                        placeholder="PW"
                        onChange={(e) => setPw(e.target.value)}
                      />
                    </CInputGroup>
                    <CRow>
                      <CCol xs={6}>
                        <CButton type="submit" color="primary" className="px-4">로그인</CButton>
                      </CCol>
                      <CCol xs={6} className="text-right">
                        <CButton color="link" className="px-0">
                          비밀번호 찾기
                        </CButton>
                      </CCol>
                    </CRow>
                  </CForm>
                </CCardBody>
              </CCard>
              <CCard className="text-white bg-primary py-5" style={{ width: '44%' }}>
                <CCardBody className="text-center">
                  <div>
                    <h2>식물 키우기!</h2>
                    <p>
                      가드너가 되지 못한 당신을 위한 해결 방안
                    </p>
                    <Link to="/register">
                      <CButton color="primary" className="mt-3" active tabIndex={-1}>
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
