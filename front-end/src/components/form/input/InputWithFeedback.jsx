import {Input} from "antd";
import InputFeedbackSpan from "../../etc/InputFeedbackSpan";
import React from "react";

const InputWithFeedback = ({name, label, labelColor, size, onChange, defaultValue, feedbackMsg, className}) => {
  const style = {fontSize: "0.9em"};
  return (
    <div style={{marginBottom: "0.5rem"}} className={className}>
      <span className={`text-${labelColor}`} style={style}>{label}</span>
      <div>
        <Input
          name={name}
          size={size}
          onChange={onChange}
          defaultValue={defaultValue}
        />
      </div>
      <InputFeedbackSpan feedbackMsg={feedbackMsg}/>
    </div>
  )
}

export default InputWithFeedback;
