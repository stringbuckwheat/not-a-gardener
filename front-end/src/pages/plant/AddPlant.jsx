import {useState} from "react";
import ItemForm from "src/components/form/ItemForm";
import SubmitForAddButton from "src/components/button/SubmitForAddButton";
import {useLocation} from "react-router-dom";
import getPlantFormArray from "../../utils/function/getPlantFormArray";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import postData from "../../api/backend-api/common/postData";

const AddPlant = ({placeList, addPlant, closeAddForm}) => {
  const initPlant = {
    plantName: "",
    plantSpecies: "",
    placeNo: placeList.length == 0 ? 0 : placeList[0].key,
    medium: "흙과 화분",
    earlyWateringPeriod: 0,
    birthday: ""
  }

  const [plant, setPlant] = useState(initPlant);

  const onChange = (e) => {
    const {name, value} = e.target;
    setPlant(setPlant => ({...plant, [name]: value}));
  }

  const isValid = plant.plantName != '' && placeList.length !== 0;

  const submit = async () => {
    const res = await postData("/plant", plant);
    addPlant(res);
    closeAddForm();
  }

  return (
    <ItemForm
      title="식물 추가"
      inputObject={plant}
      itemObjectArray={getPlantFormArray(placeList)}
      onChange={onChange}
      submitBtn={<ValidationSubmitButton
        className="float-end mt-2"
        isValid={isValid}
        onClickValid={submit}
        onClickInvalidMsg={isValid ? "" : "입력 내용을 확인해주세요"}
        title="식물 추가" />}/>
  )
}

export default AddPlant;
