import React, {useState} from "react";
import verifyEmail from "../../utils/function/verifyEmail";
import axios from "axios";
import VerifyAccountForm from "./VerifyAccountForm";

const VerifyAccount = ({successContent, setEmail, setMemberList}) => {

  const [input, setInput] = useState("");
  const [isWaiting, setIsWaiting] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");
  const [identificationCode, setIdentification] = useState("");
  const [inputIdCode, setInputIdCode] = useState("");
  const [identify, setIdentify] = useState("pending");

  const onChange = (e) => {
    setInput(e.target.value);

    if (errorMsg !== "") {
      setErrorMsg("");
    }
  }

  const submitEmail = async () => {
    setIsWaiting(true);

    try {
      const res = await axios.get(`/member/email/${input}`);

      setIdentification(res.data.identificationCode);
      setEmail(res.data.email);
      setMemberList(res.data.members);
    } catch (e) {
      setErrorMsg(e.response.data.errorDescription);
    }

    setIsWaiting(false);
  }

  const getFeedbackMsg = () => {
    if (errorMsg !== "") {
      return errorMsg;
    } else if (input !== "" && !verifyEmail(input)) {
      return "이메일 형식을 확인해주세요";
    } else if (isWaiting) {
      return "잠시만 기다려주세요";
    } else if (identificationCode !== "") {
      return "본인 확인 이메일이 발송되었습니다."
    } else {
      return "";
    }
  }

  const onChangeIdCode = (e) => {
    setInputIdCode(e.target.value);
  }

  const submitIdCode = () => {
    if (inputIdCode === identificationCode) {
      setIdentify("fulfilled");
      return;
    }
    setIdentify("rejected");
  }

  // 완료
  if (identify === "fulfilled") {
    return (
      <>
      {successContent}
      </>
    )
  }

  return (
      <div>
        <VerifyAccountForm
          label="가입 시 제출한 이메일을 입력해주세요"
          handleInput={onChange}
          defaultValue={input}
          onClick={submitEmail}
          buttonTitle="인증"
          feedbackMsg={getFeedbackMsg()}/>
        {
          identificationCode !== ""
            ?
            <VerifyAccountForm
              label="이메일로 전송된 본인확인 코드 여섯자리를 입력해주세요"
              handleInput={onChangeIdCode}
              onClick={submitIdCode}
              buttonTitle="확인"
              feedbackMsg={identify === "rejected" ? "본인확인 코드가 일치하지 않아요" : ""}/>
            : <></>
        }
      </div>
  )
}

export default VerifyAccount
