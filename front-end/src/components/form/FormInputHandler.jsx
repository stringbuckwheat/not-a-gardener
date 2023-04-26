import FormInputSelect from './input/FormInputSelect'
import FormInput from './input/FormInput'
import FormInputDate from './input/FormInputDate';

const FormInputHandler = ({itemObjectArray, onChange, inputObject}) => {
  return (
    itemObjectArray.map((inputItem, index) => {
        if (inputItem.inputType === "select") {
          return (
            <FormInputSelect
              key={index}
              inputItem={inputItem}
              onChange={onChange}/>
          )

        } else if (inputItem.inputType === "text") {

          const invalidMsg = () => {
            if (inputObject.placeName == "") {
              return "장소 이름은 비워둘 수 없어요";
            } else if (inputObject.plantName == "") {
              return "식물 이름은 비워둘 수 없어요";
            } else if (inputObject.chemicalName == "") {
              return "비료/살충/살균제 이름은 비워둘 수 없어요";
            }

            return "";
          }

          return (
            <FormInput
              key={index}
              inputItem={inputItem}
              onChange={onChange}
              feedbackInvalid={invalidMsg()}
            />
          )

        } else if (inputItem.inputType === "number") {
          const invalidMsg = () => {
            if (!Number.isInteger(inputObject.chemicalPeriod)) {
              return "숫자를 입력해주세요"
            } else if (!Number.isInteger(inputObject.averageWateringPeriod)) {
              return "숫자를 입력해주세요."
            }

            return "";
          }

          return (
            <FormInput
              key={index}
              inputItem={inputItem}
              onChange={onChange}
              feedbackInvalid={invalidMsg()}
            />
          )

        } else if (inputItem.inputType === "date") {
          return (
            <FormInputDate
              key={index}
              inputItem={inputItem}
              onChange={onChange}/>
          )

        }
      }
    ))
}

export default FormInputHandler
