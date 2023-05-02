import {useState} from "react"
import ItemForm from "src/components/form/ItemForm"
import chemicalTypeArray from "src/utils/dataArray/chemicalTypeArray"
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import postData from "../../api/backend-api/common/postData";
import getChemicalFormArray from "../../utils/function/getChemicalFormArray";

const AddChemical = ({addChemical, afterAdd}) => {
  const [chemical, setChemical] = useState({
    name: "",
    type: chemicalTypeArray[0].value,
    period: 14,
    active: "Y"
  })

  const onChange = (e) => {
    const {name, value} = e.target;
    setChemical(setChemical => ({...chemical, [name]: value}));
  }

  const isValid = chemical.name != ''
    && Number.isInteger(chemical.period * 1)
    && (chemical.period * 1) > 0;

  const submit = async () => {
    const res = await postData("/chemical", chemical);
    console.log("add chemical res", res);
    addChemical(res);
    afterAdd && afterAdd();
  }

  return (
    <ItemForm
      title="비료/살균/살충제 추가"
      inputObject={chemical}
      itemObjectArray={getChemicalFormArray(chemical)}
      onChange={onChange}
      submitBtn={<ValidationSubmitButton
        className="float-end mt-2"
        isValid={isValid}
        onClickValid={submit}
        onClickInvalidMsg={isValid ? "" : "입력 내용을 확인해주세요"}
        title="약품 추가" />}/>
  )
}

export default AddChemical
