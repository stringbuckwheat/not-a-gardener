import {Button} from "antd";
import React, {useState} from "react";
import InputFeedbackSpan from "../etc/InputFeedbackSpan";

/**
 * submit시 form validation 값에 따라 회색버튼 / 오렌지색 버튼
 *
 * @param isValid boolean 유효성 검사
 * @param onClickValid data가 유효할 시(submit 가능 시) 실행할 함수
 * @param onClickInvalidMsg 유효하지 않은 데이터로 전송하려할 때 띄울 메시지
 * @param title 버튼 이름
 * @param className
 * @param size 사이즈
 * @returns {JSX.Element} 버튼
 */
const ValidationSubmitButton = ({isValid, onClickValid, onClickInvalidMsg, title, className, size}) => {
  const [invalidMsg, setInvalidMsg] = useState("");
  const [isSubmitted, setIsSubmitted] = useState(false);

  const onSubmit = () => {
    setIsSubmitted(true);
    onClickValid();
  }

  if(!isValid){
    return (
      <>
        <div className={className}>
          <div>
            <InputFeedbackSpan feedbackMsg={invalidMsg}/>
          </div>
          <Button
            size={size}
            className={`bg-light text-dark ${className}`}
            onClick={() => setInvalidMsg(onClickInvalidMsg)}>
            {title}
          </Button>
        </div>
      </>
    )
  }

  return (
    <Button
      type="button"
      size={size}
      className={`bg-orange text-white ${className}`}
      onClick={onSubmit}
      disabled={isSubmitted}
    >
      {title}
    </Button>
  )
}

export default ValidationSubmitButton;
