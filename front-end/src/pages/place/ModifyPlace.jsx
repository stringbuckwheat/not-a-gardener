import {useState} from "react";
import ItemForm from "src/components/form/ItemForm";
import getPlaceInputItemArray from "src/utils/function/getPlaceInputItemArray";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import updateData from "../../api/backend-api/common/updateData";
import {useNavigate} from "react-router-dom";

/**
 * 장소 수정
 * @param props
 * @returns {JSX.Element} ItemForm (자동 폼 만들기)
 * @constructor
 */
const ModifyPlace = (props) => {
  const navigate = useNavigate();
  const changeModifyState = props.changeModifyState;
  const [place, setPlace] = useState(props.place);

  const onChange = (e) => {
    const {name, value} = e.target;
    setPlace(setPlace => ({...place, [name]: value}));
  }

  const isValid = place.placeName !== "";

  const submit = async () => {
    const res = await updateData("/place", place.placeNo, place);
    navigate("", {replace: true, state: res});
    changeModifyState();
  }

  return (
    <ItemForm
      title="장소 수정"
      inputObject={place}
      itemObjectArray={getPlaceInputItemArray(place)}
      onChange={onChange}
      submitBtn={<ValidationSubmitButton className="float-end" isValid={isValid} title={"수정하기"} onClickValid={submit} onClickInvalidMsg={"입력값을 확인해주세요"} />}
    />
  )
}

export default ModifyPlace;
