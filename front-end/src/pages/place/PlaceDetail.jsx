import { useLocation } from 'react-router-dom'
import SmartTable from 'src/components/table/SmartTable';
import DefaultForm from 'src/components/form/DefaultForm';

const PlaceDetail = () => {
  // props
  const { state } = useLocation();
  console.log("state", state);

  const isNew = state.placeNo === undefined;

  const title = "장소";
  const action = isNew ? "추가" : "수정";

  const optionArray = [
    { key: "실내", value: "실내" },
    { key: "베란다", value: "베란다" },
    { key: "야외", value: "야외" }
  ];

  const artificialLightArray = [
    { key: "사용", value: "사용합니다" },
    { key: "미사용", value: "사용하지 않습니다" }
  ]

  const itemObjectArray = [
    {
      inputType: "text",
      label: "장소이름",
      name: "placeName",
      defaultValue: state.placeName,
      required: true
    },
    {
      inputType: "select",
      label: "이 장소의 위치",
      name: "option",
      defaultValue: state.option,
      optionArray: optionArray
    },
    {
      inputType: "select",
      label: "이 장소는 식물등을",
      name: "artificialLight",
      defaultValue: state.artificialLight,
      optionArray: artificialLightArray
    }
  ];

  const tableHeadArr = ["식물 이름", "종", "식재 환경", "평균 물주기", "createDate"];
  const keySet = ["plantName", "plantSpecies", "medium", "averageWateringPeriod", "createDate"]

  return (
    <>
    {
      isNew
      ?
      <DefaultForm
        title={title}
        action={action}
        inputObject={state}
        itemObjectArray={itemObjectArray}
        isNew={isNew}
        submitUrl="/place"
        tableHeadArr={tableHeadArr}
        list={state.plantList}
        keySet={keySet}
        linkUrl="/plant/"
      />
      :
      <DefaultForm
        title={title}
        action={action}
        inputObject={state}
        itemObjectArray={itemObjectArray}
        isNew={isNew}
        path={isNew ? undefined : state.placeNo}
        submitUrl="/place"
        tableHeadArr={tableHeadArr}
        list={state.plantList}
        keySet={keySet}
        linkUrl="/plant/"
        table={<SmartTable list={state.plantList} />}
      />
    }
      
    </>
  );
}

export default PlaceDetail;