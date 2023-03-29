import {CButton} from "@coreui/react";
import {useNavigate} from "react-router-dom";
import insertData from "src/api/backend-api/common/insertData";

const SubmitForAddButton = (props) => {
  // props
  const {url, data, validation} = props;

  const navigate = useNavigate();

  const defaultCallBack = () => {
    navigate(url, {replace: true});
  }

  const callBackFunction = props.callBackFunction ? props.callBackFunction : defaultCallBack;

  const onClick = async () => {
    const res = await insertData(url, data);
    callBackFunction(res);
  }

  return (
    validation
      ? <CButton type="button" color="success" onClick={onClick} className="float-end mt-1">추가</CButton>
      : <CButton type="button" color="secondary" variant="outline" disabled className="float-end mt-1">추가</CButton>
  )
}

export default SubmitForAddButton;
