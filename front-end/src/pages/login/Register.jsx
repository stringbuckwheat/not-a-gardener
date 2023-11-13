import React, {useState} from 'react'
import {Navigate, useNavigate} from 'react-router-dom'
import axios from 'axios'
import {Card, Form, Space} from "antd";
import setLocalStorage from "../../api/service/setLocalStorage";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import LoginPageWrapper from "./LoginPageWrapper";
import FormInputText from "./FormInputText";
import Style from './Register.module.scss'

const Register = () => {
  if (localStorage.getItem("accessToken")) {
    return <Navigate to="/" replace={true}/>
  }

  // submit용 객체
  const [register, setRegister] = useState({
    username: "",
    email: "",
    name: " ",
    password: ""
  })

  // 상태
  const [usernameCheck, setUsernameCheck] = useState('rejected'); // success, error, validating
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

    setUsernameCheck('validating')

    const res = await axios.get(`${process.env.REACT_APP_API_URL}/register/username/${username}`);
    setUsernameCheck(res.data == "" ? "success" : "error");
  }

  const navigate = useNavigate();

  // 제출
  const onSubmit = async (e) => {
    console.log("register", register);

    const res = await axios.post(`${process.env.REACT_APP_API_URL}/register`, register);
    await setLocalStorage(res.data);

    navigate('/', {replace: true});

  } // end for onSubmit

  const isValid = usernameCheck === "success"
    && idRegex.test(register.username)
    && pwRegex.test(register.password)
    && emailRegex.test(register.email)
    && register.name !== ''
    && repeatPw;

  const usernameFeedback = {
    "warning": "영문 소문자 혹은 숫자, 6자 이상 20자 이하여야해요.",
    "error": "이미 사용중인 아이디예요."
  }

  const getUsernameValidateStatus = () => {
    if (register.username == "") {
      return "";
    } else if (!idRegex.test(register.username)) {
      return "warning";
    } else if (usernameCheck !== "success") {
      return usernameCheck; // validating, error
    } else if (usernameCheck === "success" && idRegex.test(register.username)) {
      return "success";
    }
  }

  const registerForm = [
    {
      name: "username",
      label: "ID",
      onChange: (e) => username(e.target.value),
      validateStatus: getUsernameValidateStatus(),
      help: usernameFeedback,
    },
    {
      name: "name",
      label: "이름",
      onChange: onChange,
      validateStatus: register.name === '' ? "warning" : "",
      help: {"warning": "이름은 비워둘 수 없어요"},
    },
    {
      name: "email",
      label: "이메일",
      onChange: onChange,
      validateStatus: register.email == "" ? "" : !emailRegex.test(register.email) ? "warning" : "success",
      help: {"warning": "이메일 형식을 확인해주세요. 계정 찾기 시에 필요합니다!"},
    },
    {
      validateStatus: register.password == "" ? "" : !pwRegex.test(register.password) ? "warning" : "success",
      help: {"warning": "숫자, 특수문자를 포함하여 8자리 이상이어야 해요."},
      type: "password",
      label: "비밀번호",
      name: "password",
      onChange: onChange
    },
    {
      validateStatus: register.password == "" ? "" : !repeatPw ? "warning" : "success",
      help: {"warning": "비밀번호를 확인해주세요"},
      type: "password",
      label: "비밀번호 확인",
      onChange: (e) => setRepeatPw(register.password === e.target.value)
    }
  ]

  return (
    <LoginPageWrapper>
      <Card>
        <Space className="mb-3">
          <h3 className="text-success">새로운 가드너님, 반갑습니다! <span className={Style.greeting}>🤚</span></h3>
        </Space>

        {/* 회원가입 폼 */}
        <Form layout="vertical">
          {
            registerForm.map(input =>
              <FormInputText
                validateStatus={input.validateStatus}
                help={input.help}
                type={input.type}
                label={input.label}
                name={input.name}
                onChange={input.onChange}
                required={true}
              />)
          }

          <div className="d-grid">
            <ValidationSubmitButton
              isValid={isValid}
              onClickValid={onSubmit}
              onClickInvalidMsg={!isValid ? "입력한 정보를 확인해주세요" : ""}
              title="가입하기"/>
          </div>
        </Form>
      </Card>
    </LoginPageWrapper>
  )
}


export default Register
