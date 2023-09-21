import {useState} from "react";
import FormProvider from "src/components/form/FormProvider";
import getPlantFormArray from "../../utils/function/getPlantFormArray";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import postData from "../../api/backend-api/common/postData";
import {useDispatch, useSelector} from "react-redux";

const AddPlant = ({afterAdd}) => {
  const places = useSelector(state => state.places.places)
  const placesForSelect = places.map((p) => ({
    key: p.id,
    value: p.name
  }));
  const dispatch = useDispatch();

  console.log("placeForSelect", placesForSelect);

  const [plant, setPlant] = useState({
    name: "",
    species: "",
    placeId: placesForSelect.length == 0 ? 0 : placesForSelect[0].key,
    medium: "흙과 화분",
    earlyWateringPeriod: 0,
    birthday: ""
  });

  const onChange = (e) => {
    const {name, value} = e.target;
    setPlant(() => ({...plant, [name]: value}));
    console.log("onChangePlant", plant);
  }

  const isValid = plant.name != '' && plant.placeId !== 0;

  const submit = async () => {
    const res = await postData("/plant", plant);
    console.log("res", res);

    // redux 업데이트
    dispatch({type: "addPlant", payload: res});

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
        className="float-end mt-2"
        isValid={isValid}
        onClickValid={submit}
        onClickInvalidMsg={isValid ? "" : "입력 내용을 확인해주세요"}
        title="식물 추가"/>}/>
  )
}

export default AddPlant;
