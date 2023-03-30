import React, {useState} from 'react'
import {useNavigate} from 'react-router-dom'
import {
  CButton,
  CForm,
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import {cilHappy, cilLockLocked, cilUser} from '@coreui/icons'
import axios from 'axios'
import Booped from "../../components/animation/Booped";
import {Space} from "antd";
import getLogin from "../../api/backend-api/member/getLogin";
import MemberFormWrapper from "../../components/form/wrapper/MemberFormWrapper";
import FormInputFeedback from "../../components/form/input/FormInputFeedback";

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
  const [repeatPw, setRepeatPw] = useState(false);

  // input 값의 변동이 있을 시 객체 데이터 setting
  const onChange = (e) => {
    // 객체 세팅
    const {name, value} = e.target;
    setRegister(setRegister => ({...register, [name]: value}));
  }

  // 유효성 검사
  // 아이디: 반드시 영문으로 시작 숫자+언더바/하이픈 허용 6~20자리
  const idRegex = /^[A-Za-z]{1}[A-Za-z0-9_-]{5,19}$/
  // 비밀번호: 숫자, 특문 각 1회 이상, 영문은 2개 이상 사용하여 8자리 이상 입력
  const pwRegex = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/;
  // 이메일
  const emailRegex = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/

  const username = async (username) => {
    setRegister(setRegister => ({...register, username: username}));

    if (!idRegex.test(username)) {
      return;
    }

    const res = await axios.post("/register/idCheck", {'username': username});
    setUsernameCheck(res.data == "");
  }

  const navigate = useNavigate();

  // 제출
  const onSubmit = async (e) => {
    e.preventDefault();

    await axios.post("/register", register);

    await getLogin({
      username: register.username,
      pw: register.pw
    });

    navigate('/', {replace: true});

  } // end for onSubmit

  return (
    <div className="bg-light min-vh-100 d-flex flex-row align-items-center">
      <MemberFormWrapper>
        <CForm>
          <Space>
            <span style={{fontSize: "2em"}} className="text-success">반갑습니다</span>
            <Booped rotation={20} timing={200}>
              <CIcon icon={cilHappy} height={40} className="text-success"/>
            </Booped>
          </Space>
          <p className="text-dark">가입하기</p>
          <FormInputFeedback
            label={<CIcon icon={cilUser}/>}
            placeholder="아이디"
            name="username"
            required
            valid={idRegex.test(register.username) && usernameCheck}
            invalid={!idRegex.test(register.username) || !usernameCheck}
            feedbackInvalid={!idRegex.test(register.username) ? "영문 소문자 혹은 숫자, 6자 이상 20자 이하여야해요." : "이미 사용중인 아이디예요."}
            feedbackValid="사용 가능한 아이디입니다"
            onChange={(e) => username(e.target.value)}
          />
          <FormInputFeedback
            label={<CIcon icon={cilUser}/>}
            placeholder="이름"
            name="name"
            required
            valid={register.name !== ''}
            invalid={register.name === ''}
            feedbackInvalid="이름은 비워둘 수 없어요."
            onChange={onChange}
          />
          <FormInputFeedback
            label="@"
            placeholder="이메일"
            name="email"
            required
            valid={emailRegex.test(register.email)}
            invalid={!emailRegex.test(register.email)}
            feedbackInvalid={register.email == "" ? "" : "이메일 형식을 확인해주세요"}
            onChange={onChange}
          />
          <FormInputFeedback
            label={<CIcon icon={cilLockLocked}/>}
            type="password"
            placeholder="비밀번호"
            name="pw"
            onChange={onChange}
            required
            valid={pwRegex.test(register.pw)}
            invalid={!pwRegex.test(register.pw)}
            feedbackValid="사용 가능한 비밀번호입니다"
            feedbackInvalid="숫자, 특수문자를 포함하여 8자리 이상이어야 해요."/>

          <FormInputFeedback
            label={<CIcon icon={cilLockLocked}/>}
            type="password"
            placeholder="비밀번호 확인"
            onChange={(e) => setRepeatPw(e.target.value === register.pw)}
            required
            valid={repeatPw}
            invalid={!repeatPw}
            feedbackInvalid="비밀번호를 확인해주세요"
          />

          <div className="d-grid">
            {usernameCheck && idRegex.test(register.username) && pwRegex.test(register.pw) && emailRegex.test(register.email) && register.name !== '' && repeatPw
              ? <CButton type="button" color="success" onClick={onSubmit}>가입하기</CButton>
              : <CButton type="button" color="secondary" disabled>가입하기</CButton>
            }
          </div>
        </CForm>
      </MemberFormWrapper>
    </div>
  )
}


export default Register
