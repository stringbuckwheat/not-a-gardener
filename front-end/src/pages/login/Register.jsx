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
import setGardener from "../../api/service/setGardener";
import GardenerFormWrapper from "../../components/form/wrapper/GardenerFormWrapper";
import FormInputFeedback from "../../components/form/input/FormInputFeedback";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";

const Register = () => {
  // submit용 객체
  const [register, setRegister] = useState({
    username: "",
    email: "",
    name: "",
    password: ""
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

    const res = await axios.get(`/register/username/${username}`);
    setUsernameCheck(res.data == "");
  }

  const navigate = useNavigate();

  // 제출
  const onSubmit = async (e) => {
    e.preventDefault();
    console.log("register", register);

    const res = await axios.post("/register", register);
    await setGardener(res.data);

    navigate('/', {replace: true});

  } // end for onSubmit

  const isValid = usernameCheck
    && idRegex.test(register.username)
    && pwRegex.test(register.password)
    && emailRegex.test(register.email)
    && register.name !== ''
    && repeatPw;

  return (
      <GardenerFormWrapper>
        <CForm>
          <Space className="mb-3">
            <span style={{fontSize: "2em"}} className="text-success">새로운 가드너님, 반갑습니다</span>
            <Booped rotation={20} timing={200}>
              <CIcon icon={cilHappy} height={35} className="text-success"/>
            </Booped>
          </Space>

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
            name="password"
            onChange={onChange}
            required
            valid={pwRegex.test(register.password)}
            invalid={!pwRegex.test(register.password)}
            feedbackValid="사용 가능한 비밀번호입니다"
            feedbackInvalid="숫자, 특수문자를 포함하여 8자리 이상이어야 해요."/>

          <FormInputFeedback
            label={<CIcon icon={cilLockLocked}/>}
            type="password"
            placeholder="비밀번호 확인"
            onChange={(e) => setRepeatPw(e.target.value === register.password)}
            required
            valid={repeatPw}
            invalid={!repeatPw}
            feedbackInvalid="비밀번호를 확인해주세요"
          />

          <div className="d-grid">
            <ValidationSubmitButton
              isValid={isValid}
              onClickValid={onSubmit}
              onClickInvalidMsg={!isValid ? "입력한 정보를 확인해주세요" : ""}
              title="가입하기" />
          </div>
        </CForm>
      </GardenerFormWrapper>
  )
}


export default Register
