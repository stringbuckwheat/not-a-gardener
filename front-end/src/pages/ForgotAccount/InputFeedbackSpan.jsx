import React from "react";

const InputFeedbackSpan = ({feedbackMsg, className}) => {

  return (
    <span className={`${className} text-danger`} style={{fontSize: "0.8em"}}>{feedbackMsg}</span>
  )
}

export default InputFeedbackSpan;
