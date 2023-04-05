import {CButton} from "@coreui/react";
import {useNavigate} from "react-router-dom";
import postData from "src/api/backend-api/common/postData";

const SubmitForAddButton = ({url, data, validation, callBack}) => {
  const navigate = useNavigate();

  const defaultCallBack = () => {
    navigate(url, {replace: true});
  }

  const callBackFunction = callBack ? callBack : defaultCallBack;

  const onClick = async () => {
    const res = await postData(url, data);
    callBackFunction(res);
  }

  return (
    validation
      ? <CButton type="button" color="success" onClick={onClick} className="float-end mt-1">추가</CButton>
      : <CButton type="button" color="secondary" variant="outline" disabled className="float-end mt-1">추가</CButton>
  )
}

export default SubmitForAddButton;
