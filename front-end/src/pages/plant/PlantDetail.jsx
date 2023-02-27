import { useLocation } from "react-router-dom";
import DefaultForm from "src/components/form/DefaultForm";

const PlantDetail = () => {
    const { state } = useLocation();
    console.log("state", state);
    const isNew = state.plantNo == undefined;

    const title = "식물";
    const action = isNew ? "추가" : "수정";

    const itemObjectArray = [
        {
          inputType: "text",
          label: "식물 이름",
          name: "plantName",
          defaultValue: state.plantName,
          required: true
        },
        {
            inputType: "text",
            label: "식물 종",
            name: "plantSpecies",
            defaultValue: state.plantSpecies,
            required: false
        },
        {
          inputType: "select",
          label: "장소",
          name: "place",
          defaultValue: state.place,
          optionArray:["장소1", "장소2", "장소3"]
        },
        {
            inputType: "select",
            label: "식재 환경",
            name: "medium",
            defaultValue: state.medium,
            optionArray:["흙과 화분", "반수경", "수경"]
        },
        {
            inputType: "number",
            label: "평균 물주기",
            name: "earlyWateringPeriod",
            defaultValue: state.earlyWateringPeriod,
        }
      ];

    return(
        <DefaultForm 
            title={title} 
            action={action}
            inputObject={state}
            itemObjectArray={itemObjectArray} 
            isNew={isNew}
            path={isNew ? undefined : state.plantNo}
            submitUrl="/place" />
    )
}

export default PlantDetail;