import mediumArray from "../dataArray/mediumArray";

const getPlantFormArrayWithPlaceName = (placeName) => {
  const plantFormArr = [
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
      inputType: "text",
      label: "장소",
      name: "placeNo",
      defaultValue: placeName,
      disabled: true
    },
    {
      inputType: "select",
      label: "식재 환경",
      name: "medium",
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
  ]

  return plantFormArr;
}

export default getPlantFormArrayWithPlaceName;
