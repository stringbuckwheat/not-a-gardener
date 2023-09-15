import {CInputGroup, CInputGroupText, CFormInput} from "@coreui/react";

/**
 * FormInputHandler에서 쓸 Input 컴포넌트
 * @param inputItem Input의 정보를 담은 객체
 * @param onChange
 * @param feedbackInvalid
 * @returns {JSX.Element}
 * @constructor
 */
const Input = ({inputItem, onChange, feedbackInvalid}) => {
  return (
    <CInputGroup className="mb-3 mt-3">
      <CInputGroupText id="basic-addon1">{inputItem.label}</CInputGroupText>
      <CFormInput
        disabled={inputItem.disabled}
        defaultValue={inputItem.defaultValue}
        type={inputItem.inputType}
        aria-describedby="basic-addon1"
        name={inputItem.name}
        required={inputItem.required}
        onChange={onChange}
        min={"1"}
        feedbackInvalid={feedbackInvalid}
        feedbackValid={inputItem.required ? "" : "모르겠다면 비워둬도 좋아요"}
      />
    </CInputGroup>
  )
}

export default Input;
