import {CButton} from "@coreui/react";
import {useNavigate} from "react-router-dom";
import updateData from "src/api/backend-api/common/updateData";

const ModifySubmitButton = ({data, url, changeModifyState, validation}) => {
  const navigate = useNavigate();

  const submit = async () => {
    const res = await updateData(url, data);
    navigate("", {replace: true, state: res});
    changeModifyState();
  }

  return validation
    ? (
      <CButton type="button" color="success" onClick={submit}>수정</CButton>
    ) : (
      <CButton type="button" color="secondary" variant="outline" disabled>수정</CButton>
    )
}

export default ModifySubmitButton;
