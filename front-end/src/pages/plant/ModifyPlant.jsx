import { useLocation } from "react-router-dom";
import DefaultForm from "src/components/form/DefaultForm";
import mediumArray from "src/utils/mediumArray";

const ModifyPlant = () => {
    const { state } = useLocation();
    console.log("state", state);

    const itemObjectArray = [
        {
          inputType: "text",
          label: "식물 이름",
          name: "plantName",
          defaultValue: state.data.plantName,
          required: true
        },
        {
            inputType: "text",
            label: "식물 종",
            name: "plantSpecies",
            defaultValue: state.data.plantSpecies,
            required: false
        },
        {
          inputType: "select",
          label: "장소",
          name: "placeNo",
          defaultValue: state.data.placeNo,
          optionArray: state.placeList
        },
        {
            inputType: "select",
            label: "식재 환경",
            name: "medium",
            defaultValue: state.data.medium,
            optionArray: mediumArray.map((medium) => {
              return {key: medium, value: medium}
            })
        },
        {
            inputType: "number",
            label: "평균 물주기",
            name: "averageWateringPeriod",
            defaultValue: state.data.averageWateringPeriod,
        }
      ];

    return(
        <DefaultForm 
            title="식물"
            inputObject={state.data}
            itemObjectArray={itemObjectArray} 
            isNew={false}
            path={state.data.plantNo}
            submitUrl="/plant" />
    )
}

export default ModifyPlant;