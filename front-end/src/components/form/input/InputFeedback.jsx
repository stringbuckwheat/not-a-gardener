import {CFormInput, CInputGroup, CInputGroupText} from "@coreui/react";
import React from "react";

const InputFeedback = ({
                             label,
                             type,
                             placeholder,
                             name,
                             required,
                             valid,
                             invalid,
                             feedbackInvalid,
                             feedbackValid,
                             onChange,
                             defaultValue,
                             disabled
                           }) => {
  return (
    <CInputGroup className="mb-3">
      <CInputGroupText>{label}</CInputGroupText>
      <CFormInput
        type={type ? type : "text"}
        defaultValue={defaultValue}
        disabled={disabled}
        placeholder={placeholder}
        name={name}
        required={required}
        valid={valid}
        invalid={invalid}
        feedbackInvalid={feedbackInvalid}
        feedbackValid={feedbackValid}
        onChange={onChange}/>
    </CInputGroup>
  )
}

export default InputFeedback;
