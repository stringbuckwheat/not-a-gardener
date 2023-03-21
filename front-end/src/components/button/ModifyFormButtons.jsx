import { CButton } from "@coreui/react";
import { Space } from "antd";
import ModifySubmitButton from "./ModifySubmitButton";

const ModifyFormButtons = (props) => {
    const validation = props.validation;
    const data = props.data;
    const url = props.url; // 제출할 주소 ex. plant, place...
    const path = props.path; // PathVariable
    const changeModifyState = props.changeModifyState; // modify state 변경 함수

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