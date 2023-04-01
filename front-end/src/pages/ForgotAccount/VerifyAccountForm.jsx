import {Button, Input, Space} from "antd";
import React from "react";

const VerifyAccountForm = (props) => {
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
      <span className="text-danger" style={{fontSize: "0.8em"}}>{feedbackMsg}</span>
    </div>
  )
}

export default VerifyAccountForm;
