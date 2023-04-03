import {Button, Input, Space} from "antd";
import React from "react";
import InputFeedbackSpan from "../../../components/etc/InputFeedbackSpan";

const VerifyAccountInput = (props) => {
  const {label, handleInput, defaultValue, onClick, buttonTitle, feedbackMsg} = props;

  return (
    <div className="mb-2">
      <span className="text-garden" style={{fontSize: "0.9em"}}>{label}</span>
      <div>
        <Space.Compact>
          <Input
            onChange={handleInput}
            defaultValue={defaultValue}
          />
          <Button className="bg-orange text-white" onClick={onClick}>{buttonTitle}</Button>
        </Space.Compact>
      </div>
      <InputFeedbackSpan feedbackMsg={feedbackMsg}/>
    </div>
  )
}

export default VerifyAccountInput;
