import {CButton} from "@coreui/react";
import {Space} from "antd";
import ModifySubmitButton from "./ModifySubmitButton";

// TODO 제거

const ModifyFormButtons = ({validation, data, url, changeModifyState}) => {
  return (
    <div className="d-flex justify-content-end">
      <Space>
        <CButton color="light" onClick={changeModifyState}>돌아가기</CButton>
        <ModifySubmitButton
          data={data}
          url={url}
          changeModifyState={changeModifyState}
          validation={validation}/>
      </Space>
    </div>
  )
}

export default ModifyFormButtons;
