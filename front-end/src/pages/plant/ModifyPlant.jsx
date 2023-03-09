import mediumArray from "src/utils/dataArray/mediumArray";
import getPlaceList from "src/utils/function/getPlaceList";
import ModifyForm from "src/components/form/ModifyForm";

const ModifyPlant = (props) => {
  console.log("modify plant props", props);

  const title = props.title;
  const onClickGetBackBtn = props.onClickGetBackBtn;
  const plant = props.plant;
  const requiredValueArray = ["plantName"];
  const placeList = props.placeList;
  const isNumberArray = ["averageWateringPeriod"]

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
    <ModifyForm
      title={title}
      inputObject={plant}
      itemObjectArray={itemObjectArray}
      requiredValueArray={requiredValueArray}
      isNumberArray={isNumberArray}
      submitUrl="plant"
      path={plant.plantNo}
      onClickGetBackBtn={onClickGetBackBtn}
    />
  )
}

export default ModifyPlant;