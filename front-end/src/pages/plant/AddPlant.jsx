import mediumArray from "src/utils/dataArray/mediumArray";
import { useState } from "react";
import ItemForm from "src/components/form/ItemForm";
import SubmitForAddButton from "src/components/button/SubmitForAddButton";
import { useLocation } from "react-router-dom";

const AddPlant = () => {
  const placeList = useLocation().state;
  console.log("add plant == placeList", placeList);

  const initPlant = !placeList.length == 0
    ? {
      plantName: "",
      plantSpecies: "",
      placeNo: placeList[0].key,
      medium: "흙과 화분",
      earlyWateringPeriod: 0,
      birthday: ""
    }
    : {
      plantName: "",
      plantSpecies: "",
      placeNo: 0,
      medium: "흙과 화분",
      earlyWateringPeriod: 0,
      birthday: ""
    }

  const noPlace = !placeList.length == 0
    ? {}
    : {
      addUrl: "/place",
      size: "sm",
      title: "장소 추가하기"
    }

  const [plant, setPlant] = useState(initPlant);

  const onChange = (e) => {
    const { name, value } = e.target;
    setPlant(setPlant => ({ ...plant, [name]: value }));
    console.log("onchange", plant);
  }

  const validation = plant.plantName != '' && placeList.length !== 0;

  const itemObjectArray = [
    {
      inputType: "text",
      label: "식물 이름",
      name: "plantName",
      required: true
    },
    {
      inputType: "text",
      label: "식물 종",
      name: "plantSpecies",
      required: false
    },
    {
      inputType: "select",
      label: "장소",
      name: "placeNo",
      optionArray: placeList,
      noPlace: noPlace
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
      required: false
    },
    {
      inputType: "date",
      label: "반려 일자",
      name: "birthday",
      required: false
    }
  ];


  return (
    <ItemForm
      title="식물 추가"
      inputObject={plant}
      itemObjectArray={itemObjectArray}
      onChange={onChange}
      submitBtn={<SubmitForAddButton
        url="/plant"
        data={plant}
        validation={validation} />} />
  )
}

export default AddPlant;