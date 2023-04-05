import {Input} from "antd";
import InputFeedbackSpan from "../etc/InputFeedbackSpan";
import React from "react";

const InputFeedback = ({name, label, labelColor, size, onChange, defaultValue, feedbackMsg}) => {
  const style = {fontSize: "0.9em"};
  return (
    <div className="mb-2">
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

export default InputFeedback;
