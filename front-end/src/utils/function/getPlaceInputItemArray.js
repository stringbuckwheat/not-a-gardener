import placeOptionObjectArray from "src/utils/dataArray/placeOptionArray";

// defaultValue로 쓸 데이터가 담긴 place 객체를 주면
// defaultLayout, form 등에 props로 전달할 itemObjectArray를 반환한다.

const getPlaceInputItemArray = (place) => {
  return (
    [
      {
        inputType: "text",
        label: "장소 이름",
        name: "name",
        defaultValue: place.name,
        required: true
      },
      {
        inputType: "select",
        label: "이 장소의 위치",
        name: "option",
        defaultValue: place.option,
        optionArray: placeOptionObjectArray
      },
      {
        inputType: "select",
        label: "이 장소는 식물등을",
        name: "artificialLight",
        defaultValue: place.artificialLight,
        optionArray: [
          {key: "미사용", value: "사용하지 않습니다"},
          {key: "사용", value: "사용합니다"},
        ]
      }
    ]
  )
}

export default getPlaceInputItemArray;
