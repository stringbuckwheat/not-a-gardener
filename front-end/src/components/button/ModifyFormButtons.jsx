import {CButton} from "@coreui/react";
import {Space} from "antd";
import ModifySubmitButton from "./ModifySubmitButton";

const ModifyFormButtons = (props) => {
  const {validation, data, url, path, changeModifyState} = props;

  return (
    <div className="d-flex justify-content-end">
      <Space>
        <CButton color="light" onClick={changeModifyState}>돌아가기</CButton>
        <ModifySubmitButton
          data={data}
          url={url}
          path={path}
          changeModifyState={changeModifyState}
          validation={validation}/>
      </Space>
    </div>
  )
}

export default ModifyFormButtons;
