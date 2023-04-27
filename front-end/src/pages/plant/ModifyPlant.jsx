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
      name: "plantName",
      defaultValue: plant.plantName,
      required: true
    },
    {
      inputType: "text",
      label: "식물 종",
      name: "plantSpecies",
      defaultValue: plant.plantSpecies,
      required: false
    },
    {
      inputType: "select",
      label: "장소",
      name: "placeNo",
      defaultValue: plant.placeNo,
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
      name: "averageWateringPeriod",
      defaultValue: plant.averageWateringPeriod,
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
        url={`/plant/${plant.plantNo}`}
        changeModifyState={changeModifyState}
        validation={plant.plantName != ""}/>}/>
  )
}

export default ModifyPlant;
