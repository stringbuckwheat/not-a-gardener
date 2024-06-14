import {useState} from "react";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import postData from "../../api/backend-api/common/postData";
import {useDispatch, useSelector} from "react-redux";
import FormProvider from "../../components/form/FormProvider";
import mediumArray from "../../utils/dataArray/mediumArray";
import PlantAction from "../../redux/reducer/plants/plantAction";

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
      required: true,
      feedbackInvalid: "식물 이름은 비워둘 수 없어요"
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
      noPlace: noPlace,
      required: true
    },
    {
      inputType: "select",
      label: "식재 환경",
      name: "medium",
      optionArray: mediumArray,
      required: true
    },
    {
      inputType: "number",
      label: "최근 물 준 간격(일)",
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

const AddPlant = ({afterAdd}) => {
  const places = useSelector(state => state.places.places);

  const placesForSelect = places.map((p) => ({
    key: p.id,
    value: p.name
  }));

  const dispatch = useDispatch();

  const [plant, setPlant] = useState({
    name: "",
    species: "",
    placeId: placesForSelect.length == 0 ? 0 : placesForSelect[0].key,
    medium: "흙과 화분",
    earlyWateringPeriod: 0,
    birthday: ""
  });

  const onChange = (e) => {
    console.log("e", e);
    const {name, value} = e.target;
    setPlant(() => ({...plant, [name]: value}));
    console.log("onChangePlant", plant);
  }

  const isValid = plant.name != '' && plant.placeId !== 0;

  const submit = async () => {
    const res = await postData("/plant", plant);
    console.log("res", res);

    // redux 업데이트
    dispatch({type: PlantAction.ADD_PLANT, payload: res});

    // 수정 컴포넌트 닫기
    afterAdd && afterAdd();
  }

  return (
    <FormProvider
      title="식물 추가"
      inputObject={plant}
      itemObjectArray={getPlantFormArray(placesForSelect)}
      onChange={onChange}
      submitBtn={<ValidationSubmitButton
        className="float-end"
        isValid={isValid}
        onClickValid={submit}
        onClickInvalidMsg={isValid ? "" : "입력 내용을 확인해주세요"}
        title="식물 추가"/>}/>
  )
}

export default AddPlant;
