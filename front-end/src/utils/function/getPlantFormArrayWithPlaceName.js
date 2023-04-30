import mediumArray from "../dataArray/mediumArray";

const getPlantFormArrayWithPlaceName = (placeName) => {
  const plantFormArr = [
    {
      inputType: "text",
      label: "식물 이름",
      name: "name",
      required: true
    },
    {
      inputType: "text",
      label: "식물 종",
      name: "species",
      required: false
    },
    {
      inputType: "text",
      label: "장소",
      name: "placeId",
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
      label: "최근 물주기",
      name: "recentWateringPeriod",
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
