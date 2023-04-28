import React from "react";

/**
 * input 도움말
 * @param feedbackMsg ex. 식물 이름은 비워둘 수 없어요
 * @param className
 * @param color
 * @returns {JSX.Element}
 * @constructor
 */
const InputFeedbackSpan = ({feedbackMsg, className, color="danger"}) => {
  const style = {fontSize: "0.8em", transition: "all ease 2s 0s"};

  return (
    <span
      className={`${className} text-${color}`}
      style={style}>
      {feedbackMsg}
    </span>
  )
}

export default InputFeedbackSpan;
