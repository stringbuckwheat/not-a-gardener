import authAxios from "src/utils/requestInterceptor";
import { CButton } from "@coreui/react";
import { useNavigate } from "react-router-dom";

const FormSubmitButton = (props) => {
    const navigate = useNavigate();
    console.log("버튼 props", props);

    const saveOrUpdate = () => {
        console.log("data", props.data);

        if(props.path){
            console.log("수정")
            updateData();
            return;
        }

        console.log("인서트")
        insertData();
    }

    const insertData = () => {
        authAxios.post(props.url, props.data)
        .then((res) => {
            navigate(props.url);
        })
    }

    const updateData = () => {
        authAxios.put(props.url + "/" + props.path, props.data)
        .then((res) => {
            navigate(props.url);
        })
    }

    return (
        props.validation
        ? <CButton type="button" color='success' onClick={saveOrUpdate}>{props.buttonName}</CButton>
        : <CButton type="button" color='secondary' variant="outline" disabled>{props.buttonName}</CButton>
    )
}

export default FormSubmitButton;