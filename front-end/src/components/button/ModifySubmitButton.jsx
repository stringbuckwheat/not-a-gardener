import authAxios from "src/utils/interceptors";
import { CButton } from "@coreui/react";
import { useNavigate } from "react-router-dom";

const ModifySubmitButton = (props) => {
    const data = props.data; // 제출할 데이터
    const url = props.url; // 제출할 주소 ex. plant, place...
    const path = props.path; // PathVariable
    const changeModifyState = props.changeModifyState; // modify state 변경 함수
    const validation = props.validation;

    const navigate = useNavigate();

    const updateData = () => {
        authAxios.put(`${url}/${path}`, data)
            .then((res) => {
                changeModifyState();
                navigate("", { replace: true, state: res.data });
            })
    }

    return (
        <>
        {
        validation
            ? <CButton type="button" color='success' onClick={updateData}>수정</CButton>
            : <CButton type="button" color='secondary' variant="outline" disabled>수정</CButton>
        }
            </>
    )
}

export default ModifySubmitButton;
