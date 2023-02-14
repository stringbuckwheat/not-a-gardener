import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
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

const Register = () => {
  // submit용 객체
  const [register, setRegister] = useState({
    username: "",
    email: "",
    name: "",
    pw: ""
  })

  // 상태
  const [usernameCheck, setUsernameCheck] = useState(false);
  const [emailCheck, setEmailCheck] = useState(false);
  const [repeatPw, setRepeatPw] = useState(false);
  
  // input 값의 변동이 있을 시 객체 데이터 setting
  const onChange = (e) => {
    // 객체 세팅
    const {name, value} = e.target;
    setRegister(setRegister => ({...register, [name]: value }));
  }

  // 유효성 검사
  // 아이디: 반드시 영문으로 시작 숫자+언더바/하이픈 허용 6~20자리
  const idRegex = /^[A-Za-z]{1}[A-Za-z0-9_-]{5,19}$/
  // 비밀번호: 숫자, 특문 각 1회 이상, 영문은 2개 이상 사용하여 8자리 이상 입력
  const pwRegex = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/;
  // 이메일
  const emailRegex = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/

  const username = (username) => {
    // setUsernameCheck(false);
    setRegister(setRegister => ({...register, username: username }));

    if(!idRegex.test(username)){
      return;
    }

    // 서버 가서 확인
    axios.post("/idCheck", {'username': username})
    .then((res) => {
      console.log("data: " + res.data);

      if(res.data != ''){
        setUsernameCheck(false);
      } else {
        setUsernameCheck(true);
      }
    })
  }

  // 이메일 중복 확인
  const email = (email) => {
    setRegister(setRegister => ({...register, email: email}));

    if(!emailRegex.test(email)){
      return;
    }

    // 서버 가서 확인
    axios.post("/emailCheck", {'email': email})
    .then((res) => {
      console.log("email data: " + res.data);

      if(res.data != ''){
        setEmailCheck(false);
      } else {
        setEmailCheck(true);
      }
    })
  }

  const navigate = useNavigate();

  // 제출
  const onSubmit = (e) => {
    e.preventDefault();

    axios.post("/register", register)
    .then((res) => {
      // 콜백으로 로그인 실행
      const data = {username: register.username, pw: register.pw};

      axios.post("/", data)
      .then((res) => {
        // local storage에 토큰 저장
        localStorage.setItem("login", res.data);
        navigate('/garden');
      })
    })
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
                    <CFormInput placeholder="아이디" name="username" required onChange={(e) => username(e.target.value)}/>
                  </CInputGroup>
                  {
                    !idRegex.test(register.username) 
                    ? <p>아이디는 영문 소문자, 숫자, 6자 이상 20자 이하여야합니다.</p>
                    : ( usernameCheck
                      ? <p>사용 가능한 아이디입니다.</p>
                      : <p>이미 사용중인 아이디입니다.</p>
                    )
                  }
                  <CInputGroup className="mb-3">
                    <CInputGroupText>
                      <CIcon icon={cilUser} />
                    </CInputGroupText>
                    <CFormInput placeholder="이름" name="name" required onChange={onChange}/>
                  </CInputGroup>
                  <p>{register.name === '' ? "이름은 비워둘 수 없습니다." : ""}</p>
                  <CInputGroup className="mb-3">
                    <CInputGroupText>@</CInputGroupText>
                    <CFormInput placeholder="이메일" name="email" required onChange={(e) => email(e.target.value)}/>
                  </CInputGroup>
                  <p>{
                  !emailRegex.test(register.email) 
                  ? "이메일 형식을 확인해주세요" 
                  : ( emailCheck
                    ? "사용 가능한 이메일입니다."
                    : "이미 가입된 이메일입니다.")}</p>
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
                  <p>
                  {!pwRegex.test(register.pw) ? "비밀번호는 숫자, 특수문자를 포함하여 8자리 이상이어야 합니다.": ""}
                  </p>
                  <CInputGroup className="mb-4">
                    <CInputGroupText>
                      <CIcon icon={cilLockLocked} />
                    </CInputGroupText>
                    <CFormInput
                      type="password"
                      placeholder="비밀번호 확인"
                      onChange = {(e) => setRepeatPw(e.target.value === register.pw)}
                      required
                    />
                  </CInputGroup>
                  <p>{!repeatPw ? "비밀번호를 확인해주세요": ""}</p>
                  <div className="d-grid">
                  {usernameCheck && idRegex.test(register.username) && pwRegex.test(register.pw) && emailRegex.test(register.email) && emailCheck && register.name !== '' && repeatPw
                  ? <CButton type="submit" color="success" >가입하기</CButton>
                  : <CButton type="submit" color="secondary" disabled>가입하기</CButton>
                  }
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
