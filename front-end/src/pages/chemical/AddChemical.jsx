import {useState} from "react"
import ItemForm from "src/components/form/ItemForm"
import chemicalTypeArray from "src/utils/dataArray/chemicalTypeArray"
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import postData from "../../api/backend-api/common/postData";

const AddChemical = ({addChemical, closeAddForm}) => {
  const [chemical, setChemical] = useState({
    chemicalName: "",
    chemicalType: chemicalTypeArray[0].value,
    chemicalPeriod: 14
  })

  const onChange = (e) => {
    const {name, value} = e.target;
    setChemical(setChemical => ({...chemical, [name]: value}));
  }

  const itemObjectArray = [
    {
      inputType: "text",
      label: "이름",
      name: "chemicalName",
      defaultValue: "",
      required: true
    },
    {
      inputType: "select",
      label: "종류",
      name: "chemicalType",
      defaultValue: chemicalTypeArray[0].value,
      optionArray: chemicalTypeArray
    },
    {
      inputType: "number",
      label: "주기 (일)",
      name: "chemicalType",
      defaultValue: 14,
      required: true
    }
  ]

  const isValid = chemical.chemicalName != ''
    && Number.isInteger(chemical.chemicalPeriod * 1)
    && (chemical.chemicalPeriod * 1) > 0;

  const submit = async () => {
    const res = await postData("/chemical", chemical);
    addChemical(res);
    closeAddForm(); // 컴포넌트 변경
  }

  return (
    <ItemForm
      title="비료/살균/살충제 추가"
      inputObject={chemical}
      itemObjectArray={itemObjectArray}
      onChange={onChange}
      submitBtn={<ValidationSubmitButton
        className="float-end mt-2"
        isValid={isValid}
        onClickValid={submit}
        onClickInvalidMsg={isValid ? "" : "입력 내용을 확인해주세요"}
        title="비료 추가" />}/>
  )
}

export default AddChemical