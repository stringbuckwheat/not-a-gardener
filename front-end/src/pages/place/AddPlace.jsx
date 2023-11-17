import getPlaceInputItemArray from "src/utils/function/getPlaceInputItemArray";
import {useState} from "react";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import postData from "../../api/backend-api/common/postData";
import {useDispatch} from "react-redux";
import FormProvider from "../../components/form/FormProvider";

/**
 * 장소 추가
 * @param addPlace
 * @param closeAddForm
 * @returns {JSX.Element}
 * @constructor
 */
const AddPlace = ({afterAdd}) => {
  const [place, setPlace] = useState({
    name: "",
    option: "실내",
    artificialLight: "미사용"
  });

  const dispatch = useDispatch();

  const onChange = (e) => {
    const {name, value} = e.target;
    setPlace(() => ({...place, [name]: value}));
  }

  const isValid = place.name !== "";

  const submit = async () => {
    console.log("place", place);
    const res = await postData("/place", place);

    // Redux 업데이트
    dispatch({type: 'addPlace', payload: res});

    // 컴포넌트 변경
    afterAdd();
  }

  return (
    <FormProvider
      title="장소 추가"
      inputObject={place}
      itemObjectArray={getPlaceInputItemArray(place)}
      onChange={onChange}
      submitBtn={<ValidationSubmitButton
        className="float-end"
        isValid={isValid}
        onClickValid={submit}
        onClickInvalidMsg={isValid ? "" : "입력 내용을 확인해주세요"}
        title="장소 추가"/>}/>
  )
}

export default AddPlace;
