import {CButton} from "@coreui/react";
import {Space} from "antd";
import {useNavigate} from "react-router-dom";
import updateData from "../../api/backend-api/common/updateData";

const ModifyFormButtons = ({validation, data, url, changeModifyState}) => {
  const navigate = useNavigate();

  const submit = async () => {
    const res = await updateData(url, data);
    navigate("", {replace: true, state: res});
    changeModifyState();
  }

  const button = validation
    ? (
      <CButton type="button" color="success" onClick={submit}>수정</CButton>
    ) : (
      <CButton type="button" color="secondary" variant="outline" disabled>수정</CButton>
    )

  return (
    <div className="d-flex justify-content-end">
      <Space>
        <CButton color="light" onClick={changeModifyState}>돌아가기</CButton>
        {button}
      </Space>
    </div>
  )
}

export default ModifyFormButtons;
