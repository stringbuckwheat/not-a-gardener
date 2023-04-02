import {Button} from "antd";
import React, {useState} from "react";
import InputFeedbackSpan from "./InputFeedbackSpan";

const ValidationSubmitButton = ({isValid, onClickValid, onClickInvalidMsg, title}) => {
  const [invalidMsg, setInvalidMsg] = useState("");

  return isValid ? (
    <Button className="bg-orange text-white" onClick={onClickValid}>{title}</Button>
  ) : (
    <>
      <div>
        <InputFeedbackSpan feedbackMsg={invalidMsg}/>
      </div>
      <Button className="bg-light text-dark float-end" onClick={() => setInvalidMsg(onClickInvalidMsg)}>{title}</Button>
    </>
  )
}

export default ValidationSubmitButton;
