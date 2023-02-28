import { useLocation } from "react-router-dom";
import DefaultForm from "src/components/form/DefaultForm";
import { useState } from "react";

const AddPlant = () => {
    const { state } = useLocation();
    console.log("state", state);

    const initPlant = state.initPlant;
    const placeList = state.placeList;

    const isNew = state.plantNo == undefined;
    const [ initPlaceNo, setInitPlaceNo ] = useState(0);


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
          name: "placeNo",
          defaultValue: isNew ? placeList[0].key : initPlant.placeNo,
          optionArray: placeList
        },
        {
            inputType: "select",
            label: "식재 환경",
            name: "medium",
            defaultValue: state.medium,
            optionArray:[
              {key: "흙과 화분", value: "흙과 화분"},
              {key: "수태", value: "수태"},
              {key: "반수경", value: "반수경"},
              {key: "수경", value: "수경"},
              {key: "테라리움", value: "테라리움"}
            ]
        },
        {
            inputType: "number",
            label: "평균 물주기",
            name: "averageWateringPeriod",
            defaultValue: state.averageWateringPeriod,
        }
      ];

    return(
        <DefaultForm 
            title="식물"
            inputObject={initPlant}
            itemObjectArray={itemObjectArray} 
            isNew={isNew}
            path={isNew ? undefined : state.plantNo}
            submitUrl="/plant" />
    )
}

export default AddPlant;