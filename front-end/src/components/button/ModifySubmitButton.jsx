import { CButton } from "@coreui/react";
import { useNavigate } from "react-router-dom";
import updateData from "src/api/backend-api/common/updateData";

const ModifySubmitButton = (props) => {
    const data = props.data; // 제출할 데이터
    const url = props.url; // 제출할 주소 ex. plant, place...
    const path = props.path; // PathVariable
    const changeModifyState = props.changeModifyState; // modify state 변경 함수
    const validation = props.validation;

    const navigate = useNavigate();

    const submit = async () => {
        const res = await updateData(url, path, data);
        navigate("", { replace: true, state: res });
        changeModifyState();
    }

    return (
        <>
            {
                validation
                    ? <CButton type="button" color='success' onClick={submit}>수정</CButton>
                    : <CButton type="button" color='secondary' variant="outline" disabled>수정</CButton>
            }
        </>
    )
}

export default ModifySubmitButton;
