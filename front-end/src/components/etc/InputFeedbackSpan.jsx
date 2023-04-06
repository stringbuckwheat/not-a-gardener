import React from "react";

const InputFeedbackSpan = ({feedbackMsg, className, color="danger"}) => {

  return (
    <span
      className={`${className} text-${color}`}
      style={{fontSize: "0.8em", transition: "all ease 2s 0s"}}>
      {feedbackMsg}
    </span>
  )
}

export default InputFeedbackSpan;
