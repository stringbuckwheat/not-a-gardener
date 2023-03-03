import authAxios from "src/utils/interceptors";
import { CButton } from "@coreui/react";
import { useNavigate } from "react-router-dom";

const SubmitForAddButton = (props) => {
    // props
    const url = props.url;
    const data = props.data;
    const validation = props.validation;

    const navigate = useNavigate();

    const insertData = () => {
        authAxios.post(url, data)
        .then(() => {
            navigate(url);
        })
    }

    return (
        validation
        ? <CButton type="button" color='success' onClick={insertData}>추가</CButton>
        : <CButton type="button" color='secondary' variant="outline" disabled>추가</CButton>
    )
}

export default SubmitForAddButton;
