import React, {useState} from "react";
import verifyPassword from "../../../utils/function/verifyPassword";
import {Button, Input} from "antd";
import InputFeedbackSpan from "../../../components/etc/InputFeedbackSpan";
import ValidationSubmitButton from "../../../components/button/ValidationSubmitButton";
import axios from "axios";
import {Link} from "react-router-dom";

const ChangePassword = ({username}) => {
  // 변경할 비밀번호와 비번 확인용 input
  const [password, setPassword] = useState({
    password: "",
    repeatPassword: ""
  })

  // 비밀번호 변경 완료 여부
  const [isUpdated, setIsUpdated] = useState(false);

  // input 컨트롤
  const onChange = (e) => {
    const {name, value} = e.target;
    setPassword(setPassword => ({...password, [name]: value}));
  }

  // 비밀번호 유효성 검사 피드백 메시지
  const getPasswordFeedbackMsg = () => {
    if (password.password == "") {
      return "";
    } else if (!verifyPassword(password.password)) {
      return "숫자, 특수문자를 포함하여 8자리 이상이어야 해요.";
    } else {
      return "사용 가능한 비밀번호입니다";
    }
  }

  // 비밀번호 확인 유효성 검사 피드백 메시지
  const getRepeatPasswordFeedbackMsg = () => {
    if (password.repeatPassword == "") {
      return "";
    } else if (password.password !== password.repeatPassword) {
      return "비밀번호가 일치하지 않습니다.";
    }
  }

  // 백엔드로 비밀번호 전달
  const submit = async () => {
    const data = {username, password: password.password}
    await axios.put(`${process.env.REACT_APP_API_URL}/forgot/${username}/password`, data);
    setIsUpdated(true);
  }

  // 변경 완료(isUpdated) 시 완료 페이지 렌더링
  // 변경 중일 시 비밀번호 입력 페이지 렌더링
  return isUpdated ? (
    <div className="align-center">
      <div className="text-center">
        <div>비밀번호를 변경했어요!</div>
        <Link to="/login">
          <Button className="bg-orange" style={{color: "white", marginTop: "1rem"}}>로그인 하러가기</Button>
        </Link>
      </div>
    </div>
  ) : (
    <div>
      <div style={{marginTop: "1rem"}}>
        <span className="text-garden">새로운 비밀번호를 입력해주세요</span>
        <div>
          <Input name="password" type="password" onChange={onChange} style={{width: "17rem"}}/>
        </div>
        <InputFeedbackSpan
          feedbackMsg={getPasswordFeedbackMsg()}
          color={verifyPassword(password.password) ? "success" : "danger"}/>
      </div>

      <div style={{marginTop: "1rem"}}>
        <span className="text-garden">비밀번호를 한번 더 입력해주세요</span>
        <div>
          <Input name="repeatPassword" type="password" onChange={onChange}/>
        </div>
        <InputFeedbackSpan feedbackMsg={getRepeatPasswordFeedbackMsg()}/>
      </div>

      <div style={{marginTop: "1rem", float: "right"}}>
        <ValidationSubmitButton
          isValid={verifyPassword(password.password) && password.password === password.repeatPassword}
          onClickValid={submit}
          onClickInvalidMsg="유효한 비밀번호를 입력해주세요"
          title="제출"/>
      </div>
    </div>
  )
}

export default ChangePassword;
