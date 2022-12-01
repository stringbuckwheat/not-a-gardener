import React, { useState, useEffect } from 'react'
import {
  CButton,
  CCard,
  CCardBody,
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

// 아이디 중복검사
// 패스워드 확인 검사
// 제출 검사

const Register = () => {
  // submit용 객체
  const [register, setRegister] = useState({
    id: "",
    email: "",
    name: "",
    pw: ""
  })

  // ID 중복 검사와 그에 따른 메시지를 담을 변수
  const [idCheck, setIdCheck] = useState(false);
  const [idCheckMsg, setIdCheckMsg] = useState("");

  // 비밀번호 확인 여부와 그에 따른 메시지를 담을 변수
  const [repeatPw, setRepeatPw] = useState("");
  const [pwCheck, setPwCheck] = useState(false);
  const [pwCheckMsg, setPwCheckMsg] = useState("");

  // input 값의 변동이 있을 시 객체 데이터 setting
  const onChange = (e) => {
    // 객체 세팅
    const {name, value} = e.target;
    setRegister(setRegister => ({...register, [name]: value }))
  }

  // ID 중복 검사를 위한 useEffect
  useEffect(() => {
    if(register.id == ""){
       return;
    }

    console.log("useEffect: " + register.id);

    // 아이디 중복 검사
    axios.post("/idCheck", {'id': register.id})
    .then((res) => {
      console.log(res);

      if(res.data == ''){
        setIdCheck(true);
        setIdCheckMsg(register.id + "은(는) 사용 가능한 아이디입니다.");
      } else {
        setIdCheck(false);
        setIdCheckMsg(register.id + "은(는) 이미 사용중인 아이디입니다.");
      }

      console.log("idCheck: " + idCheck)
    })
  }, [register.id]);

  // 비밀번호 검사
  useEffect(() => {
    if(register.pw == ''){
      // 비밀번호란이 공란인 경우
      setPwCheck(false);
      setPwCheckMsg("");
    } else if(repeatPw !== register.pw){
      setPwCheck(false);
      setPwCheckMsg("비밀번호를 확인해주세요");
    } else {
      setPwCheck(true);
      setPwCheckMsg("비밀번호 확인 완료");
    }

  }, [register.pw, repeatPw])


  const onSubmit = (e) => {
    e.preventDefault();

//    if(!idCheck){
//      return alert("아이디 중복 검사를 완료해주세요")
//    } else if(!pwCheck){
//      return alert("비밀번호 확인을 완료해주세요")
//    } else {
      console.log(register);

      // TODO 유효성 검사 함수 만들기

      axios.post("/register", register)
      .then((res) => {
        console.log("register form submit")
        console.log(res);
      })
 //   }

  } // end for onSubmit


  return (
    <div className="bg-light min-vh-100 d-flex flex-row align-items-center">
      <CContainer>
        <CRow className="justify-content-center">
          <CCol md={9} lg={7} xl={6}>
            <CCard className="mx-4">
              <CCardBody className="p-4">
                <CForm onSubmit={onSubmit}>
                  <h1>환영합니다</h1>
                  <p className="text-medium-emphasis">계정 생성</p>
                  <CInputGroup className="mb-3">
                    <CInputGroupText>
                      <CIcon icon={cilUser} />
                    </CInputGroupText>
                    <CFormInput placeholder="아이디" name="id" required onChange={onChange}/>
                  </CInputGroup>
                  <p>{idCheckMsg}</p>
                  <CInputGroup className="mb-3">
                    <CInputGroupText>
                      <CIcon icon={cilUser} />
                    </CInputGroupText>
                    <CFormInput placeholder="이름" name="name" required onChange={onChange}/>
                  </CInputGroup>
                  <CInputGroup className="mb-3">
                    <CInputGroupText>@</CInputGroupText>
                    <CFormInput placeholder="이메일" name="email" required onChange={onChange}/>
                  </CInputGroup>
                  <CInputGroup className="mb-3">
                    <CInputGroupText>
                      <CIcon icon={cilLockLocked} />
                    </CInputGroupText>
                    <CFormInput
                      type="password"
                      placeholder="비밀번호"
                      name="pw"
                      onChange={onChange}
                      required
                    />
                  </CInputGroup>
                  <CInputGroup className="mb-4">
                    <CInputGroupText>
                      <CIcon icon={cilLockLocked} />
                    </CInputGroupText>
                    <CFormInput
                      type="password"
                      placeholder="비밀번호 확인"
                      name="confirmPw"
                      onChange = {(e) => setRepeatPw(e.target.value)}
                      required
                    />
                  </CInputGroup>
                  <p>{pwCheckMsg}</p>
                  <div className="d-grid">
                    <CButton type="submit" color="success">가입하기</CButton>
                  </div>
                </CForm>
              </CCardBody>
            </CCard>
          </CCol>
        </CRow>
      </CContainer>
    </div>
  )
}

export default Register
