import { useLocation } from "react-router-dom";
import mediumArray from "src/utils/dataArray/mediumArray";
import AddForm from "src/components/form/AddForm";

const AddPlant = () => {
  const location = useLocation();
  const placeList = location.state;

  const initPlant = {
    plantName: "",
    plantSpecies: "",
    medium: "흙과 화분",
    earlyWateringPeriod: 5
  }

  const itemObjectArray = [
    {
      inputType: "text",
      label: "식물 이름",
      name: "plantName",
      defaultValue: initPlant.plantName,
      required: true
    },
    {
      inputType: "text",
      label: "식물 종",
      name: "plantSpecies",
      defaultValue: initPlant.plantSpecies,
      required: false
    },
    {
      inputType: "select",
      label: "장소",
      name: "placeNo",
      defaultValue: placeList[0].key,
      optionArray: placeList
    },
    {
      inputType: "select",
      label: "식재 환경",
      name: "medium",
      defaultValue: initPlant.medium,
      optionArray: mediumArray
    },
    {
      inputType: "number",
      label: "평균 물주기",
      name: "averageWateringPeriod",
      defaultValue: initPlant.averageWateringPeriod,
      required: false
    }
  ];

  const requiredValueArray = ["plantName"];
  const isNumberArray = ["averageWateringPeriod"];

  return (
    <AddForm 
            title="식물"
            inputObject={initPlant}
            itemObjectArray={itemObjectArray}
            requiredValueArray={requiredValueArray}
            isNumberArray={isNumberArray}
            submitUrl="/plant" />
  )
}

export default AddPlant;