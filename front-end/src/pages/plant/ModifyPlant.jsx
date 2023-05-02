import mediumArray from "src/utils/dataArray/mediumArray";
import {useState} from "react";
import ItemForm from "src/components/form/ItemForm";
import ModifyFormButtons from "src/components/button/ModifyFormButtons";

const ModifyPlant = ({changeModifyState, placeList, plant}) => {
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
      optionArray: placeList
    },
    {
      inputType: "select",
      label: "식재 환경",
      name: "medium",
      defaultValue: plant.medium,
      optionArray: mediumArray
    },
    {
      inputType: "number",
      label: "평균 물주기",
      name: "recentWateringPeriod",
      defaultValue: plant.recentWateringPeriod,
    }
  ];

  return (
    <ItemForm
      title="식물 수정"
      inputObject={updatedPlant}
      itemObjectArray={itemObjectArray}
      onChange={onChange}
      submitBtn={<ModifyFormButtons
        data={updatedPlant}
        url={`/plant/${plant.id}`}
        changeModifyState={changeModifyState}
        validation={plant.name != ""}/>}/>
  )
}

export default ModifyPlant;
