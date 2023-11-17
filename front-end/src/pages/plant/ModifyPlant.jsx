import mediumArray from "src/utils/dataArray/mediumArray";
import {useState} from "react";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import updateData from "../../api/backend-api/common/updateData";
import {useNavigate} from "react-router-dom";
import FormProvider from "../../components/form/FormProvider";

const ModifyPlant = ({changeModifyState, placeList, plant}) => {
  const navigate = useNavigate();
  const [updatedPlant, setUpdatedPlant] = useState(plant);

  const onChange = (e) => {
    const {name, value} = e.target;
    setUpdatedPlant(setPlant => ({...updatedPlant, [name]: value}));
  }

  const itemObjectArray = [
    {
      inputType: "text",
      label: "식물 이름",
      name: "name",
      defaultValue: plant.name,
      required: true
    },
    {
      inputType: "text",
      label: "식물 종",
      name: "species",
      defaultValue: plant.species,
      required: false
    },
    {
      inputType: "select",
      label: "장소",
      name: "placeId",
      defaultValue: plant.placeId,
      optionArray: placeList,
      required: true
    },
    {
      inputType: "select",
      label: "식재 환경",
      name: "medium",
      defaultValue: plant.medium,
      optionArray: mediumArray,
      required: true
    },
    {
      inputType: "number",
      label: "평균 물주기",
      name: "recentWateringPeriod",
      defaultValue: plant.recentWateringPeriod == 0 ? "" : plant.recentWateringPeriod,
    }
  ];

  const validation = updatedPlant.name != '';

  const submit = async () => {
    const res = await updateData(`/plant/${plant.id}`, updatedPlant);
    navigate("", {replace: true, state: res});
    changeModifyState();
  }

  return (
    <FormProvider
      title="식물 수정"
      inputObject={updatedPlant}
      itemObjectArray={itemObjectArray}
      onChange={onChange}
      submitBtn={
        <ValidationSubmitButton
          className="float-end"
          isValid={validation}
          title={"수정하기"}
          onClickValid={submit}
          onClickInvalidMsg={"입력값을 확인해주세요"}/>
      }/>
  )
}

export default ModifyPlant;
