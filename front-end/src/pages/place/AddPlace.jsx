import getPlaceInputItemArray from "src/utils/function/getPlaceInputItemArray";
import ItemForm from "src/components/form/ItemForm";
import {useState} from "react";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import postData from "../../api/backend-api/common/postData";

/**
 * 장소 추가
 * @param addPlace
 * @param closeAddForm
 * @returns {JSX.Element}
 * @constructor
 */
const AddPlace = ({addPlace, afterAdd}) => {
  const [place, setPlace] = useState({
    placeName: "",
    option: "실내",
    artificialLight: "미사용"
  });

  const onChange = (e) => {
    const {name, value} = e.target;
    setPlace(setPlace => ({...place, [name]: value}));
  }

  const itemObjectArray = getPlaceInputItemArray(place);
  const isValid = place.placeName !== "";

  const submit = async () => {
    const res = await postData("/place", place);

    // state 변경
    addPlace(res);

    // 컴포넌트 변경
    afterAdd();
  }

  return (
    <ItemForm
      title="장소 추가"
      inputObject={place}
      itemObjectArray={itemObjectArray}
      onChange={onChange}
      submitBtn={<ValidationSubmitButton
        className="float-end mt-2"
        isValid={place.placeName !== ""}
        onClickValid={submit}
        onClickInvalidMsg={isValid ? "" : "입력 내용을 확인해주세요"}
        title="장소 추가" />}/>
  )
}

export default AddPlace;
