import {useState} from "react";
import FormProvider from "src/components/form/FormProvider";
import getPlantFormArray from "../../utils/function/getPlantFormArray";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import postData from "../../api/backend-api/common/postData";

const AddPlant = ({placeList, addPlant, afterAdd}) => {
  console.log("placeList", placeList);
  const [plant, setPlant] = useState({
    name: "",
    species: "",
    placeId: placeList.length == 0 ? 0 : placeList[0].key,
    medium: "흙과 화분",
    earlyWateringPeriod: 0,
    birthday: ""
  });

  const onChange = (e) => {
    const {name, value} = e.target;
    setPlant(setPlant => ({...plant, [name]: value}));
    console.log("onChangePlant", plant);
  }

  const isValid = plant.name != '' && plant.placeId !== 0;

  const submit = async () => {
    // console.log("plant request", plant);
    const res = await postData("/plant", plant);
    console.log("res", res);
    addPlant && addPlant(res);
    afterAdd && afterAdd();
  }

  return (
    <FormProvider
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
