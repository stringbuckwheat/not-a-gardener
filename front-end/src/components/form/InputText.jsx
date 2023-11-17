import {Form, Input} from "antd";

/**
 * FormInputHandler에서 쓸 Input 컴포넌트
 * @param inputItem Input의 정보를 담은 객체
 * @param onChange
 * @param feedbackInvalid
 * @returns {JSX.Element}
 * @constructor
 */
const InputText = ({inputItem, onChange, feedbackInvalid}) => {
  return (
    <>
      <Form.Item
        label={inputItem.label}
        name={inputItem.name}
        rules={[{required: inputItem.required, message: feedbackInvalid}]}
      >
        <Input
          style={{color: "black"}}
          name={inputItem.name}
          onChange={onChange}
          defaultValue={inputItem.defaultValue} ////////////////////////////////////////////////////////
          disabled={inputItem.disabled} ////////////////////////////////////////////////////////
          type={inputItem.inputType}
          placeholder={inputItem.required ? "" : "모르겠다면 비워둬도 좋아요"}
        >
        </Input>
      </Form.Item>
    </>
  )
}

export default InputText;
