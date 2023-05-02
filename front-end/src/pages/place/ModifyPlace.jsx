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
const ModifyPlace = ({changeModifyState, place}) => {
  const navigate = useNavigate();
  const [modifyPlace, setModifyPlace] = useState(place);

  const onChange = (e) => {
    const {name, value} = e.target;
    setModifyPlace(setPlace => ({...modifyPlace, [name]: value}));
  }

  const isValid = modifyPlace.name !== "";

  const submit = async () => {
    console.log("modifyPlace", modifyPlace);
    const res = await updateData(`/place/${place.id}`, modifyPlace);
    navigate("", {replace: true, state: res});
    changeModifyState();
  }

  return (
    <ItemForm
      title="장소 수정"
      inputObject={modifyPlace}
      itemObjectArray={getPlaceInputItemArray(place)}
      onChange={onChange}
      submitBtn={<ValidationSubmitButton className="float-end" isValid={isValid} title={"수정하기"} onClickValid={submit}
                                         onClickInvalidMsg={"입력값을 확인해주세요"}/>}
    />
  )
}

export default ModifyPlace;
