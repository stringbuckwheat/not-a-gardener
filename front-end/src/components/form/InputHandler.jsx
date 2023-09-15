import InputSelect from './input/InputSelect'
import Input from './input/Input'
import InputDate from './input/InputDate';

const InputHandler = ({itemObjectArray, onChange, inputObject}) => {
  // text, number
  const invalidMsg = (inputItem) => {
    return inputItem.inputType === "text" ? invalidMsgForText(inputItem) : invalidMsgForNumber(inputItem);
  }
  const invalidMsgForText = (inputItem) => {
    return inputItem.required && inputObject.name == "" ? `${inputItem.label}은 비워둘 수 없어요` : "";
  }

  const invalidMsgForNumber = (inputItem) => {
    return !Number.isInteger(inputObject[inputItem.name]) ? "0 이상의 정수를 입력해주세요" : "";
  }

  return (
    itemObjectArray.map((inputItem) => {
        const commonProps = {key: inputItem.name, inputItem, onChange};
        const type = inputItem.inputType;

        return type === "select" ? <InputSelect {...commonProps}/>
          : type === "date" ? <InputDate {...commonProps}/>
            : <Input {...commonProps} feedbackInvalid={invalidMsg(inputItem)}/>
      }
    ))
}

export default InputHandler
