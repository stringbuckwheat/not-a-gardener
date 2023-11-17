import InputSelect from "./InputSelect";
import InputText from "./InputText";
import InputDate from "./InputDate";

const InputHandler = ({itemObjectArray, onChange, inputObject}) => {
  // text, number
  const invalidMsg = (inputItem) => {
    if (inputItem.feedbackInvalid) {
      return inputItem.feedbackInvalid;
    }

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
          : type === "date" ? <InputDate {...commonProps} feedbackInvalid={invalidMsg(inputItem)}/>
            : <InputText {...commonProps} feedbackInvalid={invalidMsg(inputItem)}/>
      }
    ))
}

export default InputHandler
