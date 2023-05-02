import mediumArray from "../dataArray/mediumArray";

const getPlantFormArray = (placeList) => {
  const noPlace = !placeList.length === 0
    ? {}
    : {
      addUrl: "/place",
      size: "sm",
      title: "장소 추가하기"
    }

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
      inputType: "select",
      label: "장소",
      name: "placeId",
      optionArray: placeList,
      noPlace: noPlace
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

export default getPlantFormArray;
