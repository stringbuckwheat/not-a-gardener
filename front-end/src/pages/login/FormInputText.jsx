import {Form, Input} from "antd";
import React from "react";

const FormInputText = ({validateStatus, help, onChange, name, label, required, type="text", placeholder}) => {
  // validateStatus: 'success', 'warning', 'error', 'validating'

  return (
    <Form.Item
      hasFeedback
      validateStatus={validateStatus}
      help={help[validateStatus]}
      label={label}
      style={{marginBottom: "1rem"}}
    >
      <Input
        name={name}
        placeholder={placeholder}
        type={type}
        onChange={onChange}
        required={required}/>
    </Form.Item>
  )
}

export default FormInputText;
