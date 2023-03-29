import {useState} from "react"
import SubmitForAddButton from "src/components/button/SubmitForAddButton"
import ItemForm from "src/components/form/ItemForm"
import chemicalTypeArray from "src/utils/dataArray/chemicalTypeArray"

const AddChemical = () => {
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

  const validation = chemical.chemicalName != ''
    && Number.isInteger(chemical.chemicalPeriod * 1)
    && (chemical.chemicalPeriod * 1) > 0;

  return (
    <ItemForm
      title="비료/살균/살충제 추가"
      inputObject={chemical}
      itemObjectArray={itemObjectArray}
      onChange={onChange}
      submitBtn={<SubmitForAddButton
        url="/chemical"
        data={chemical}
        validation={validation}/>}/>
  )
}

export default AddChemical
