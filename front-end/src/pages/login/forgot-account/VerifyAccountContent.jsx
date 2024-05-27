import React, {useState} from "react";
import verifyEmail from "../../../utils/function/verifyEmail";
import axios from "axios";
import VerifyAccountInput from "./VerifyAccountInput";
import postData from "../../../api/backend-api/common/postData";

/**
 * 아이디/비밀번호 찾기 전: 이메일 인증 후 이메일로 발송된 인증코드를 입력하는 페이지
 * @param successContent
 * @param setGardenerList
 * @returns {JSX.Element}
 * @constructor
 */
const VerifyAccountContent = ({successContent, setGardenerList}) => {
  // 이메일 input
  const [inputEmail, setInputEmail] = useState("");
  // 백엔드에서 유저의 메일로 인증 코드를 보낼 동안 대기해달라는 알림을 띄워놓기 위해
  const [isWaiting, setIsWaiting] = useState(false);
  // 제출한 이메일이 유효하지 않을 시 에러 메시지
  const [errorMsg, setErrorMsg] = useState("");
  // 이메일 확인 및 본인 확인 코드 전송 여부
  const [isIdentificationCodeSent, setIsIdentificationCodeSent] = useState(false);

  // 유저에게 입력받은 인증코드를 저장
  const [inputIdCode, setInputIdCode] = useState("");

  // 인증 단계를 저장
  // 1. pending: 유저가 인증코드를 입력하지 않은 상태
  // 2. fulfilled: 인증 완료
  // 3. rejected: 인증 실패
  const [identify, setIdentify] = useState("pending");

  // 이메일 입력 onChange 함수
  const onChange = (e) => {
    setInputEmail(e.target.value);

    if (errorMsg !== "") {
      setErrorMsg("");
    }
  }

  // 백엔드로 이메일 제출
  const submitEmail = async () => {
    setIsWaiting(true);

    try {
      const res = await axios.get(`${process.env.REACT_APP_API_URL}/forgot/email/${inputEmail}`);
      console.log("res", res.data);

      setInputEmail(res.data.email); // 유저 이메일
      setGardenerList(res.data.usernames); // 회원목록
      setIsIdentificationCodeSent(true);
    } catch (e) {
      // 이메일 인증 실패
      console.log("e", e);
      setErrorMsg(e.response.data.message);
    }

    setIsWaiting(false);
  }

  // 피드백 메시지 함수
  const getFeedbackMsg = () => {
    if (errorMsg !== "") {
      return errorMsg;
    } else if (inputEmail !== "" && !verifyEmail(inputEmail)) {
      return "이메일 형식을 확인해주세요";
    } else if (isWaiting) {
      return "잠시만 기다려주세요";
    } else if (isIdentificationCodeSent) {
      return "본인 확인 이메일이 발송되었습니다."
    } else {
      return "";
    }
  }

  // 인증코드 입력 후 '인증' 버튼 클릭 시
  const submitIdCode = async () => {
    try {
      const res = await axios.post(`${process.env.REACT_APP_API_URL}/forgot`, {
        email: inputEmail,
        identificationCode: inputIdCode
      });
      console.log("res.data", res.data);

      setIdentify(() => "fulfilled")
    } catch (e) {
      console.log("e", e);
      alert(e.response.data.message);
    }
  }

  ////// 렌더링 /////

  // 완료
  // 아이디 찾기에서 성공 시 NotifyUsername 컴포넌트를, 비밀번호 찾기에서 성공 시 SelectAccount 컴포넌트를 렌더링
  if (identify === "fulfilled") {
    return (
      <>
        {successContent}
      </>
    )
  }

  // 아직 인증 중일 경우 렌더링될 컴포넌트들
  return (
    <div>
      <VerifyAccountInput
        label="가입 시 제출한 이메일을 입력해주세요"
        handleInput={onChange}
        defaultValue={inputEmail}
        onClick={submitEmail}
        buttonTitle="인증"
        feedbackMsg={getFeedbackMsg()}/>

      <VerifyAccountInput
        label="이메일로 전송된 본인확인 코드 여섯자리를 입력해주세요"
        handleInput={(e) => setInputIdCode(e.target.value)}
        onClick={submitIdCode}
        buttonTitle="확인"
        feedbackMsg={identify === "rejected" ? "본인확인 코드가 일치하지 않아요" : ""}/>
    </div>
  )
}

export default VerifyAccountContent
