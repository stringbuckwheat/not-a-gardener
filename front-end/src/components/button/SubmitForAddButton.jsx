import { CButton } from "@coreui/react";
import { useNavigate } from "react-router-dom";
import insertData from "src/api/backend-api/common/insertData";

const SubmitForAddButton = (props) => {
    // props
    const url = props.url;
    const data = props.data;
    const validation = props.validation;

    const navigate = useNavigate();

    const onClick = async () => {
        const res = await insertData(url, data);
        navigate(url, { replace: true, state: "insert" });
    }

    return (
        validation
            ? <CButton type="button" color='success' onClick={onClick}>추가</CButton>
            : <CButton type="button" color='secondary' variant="outline" disabled>추가</CButton>
    )
}

export default SubmitForAddButton;
