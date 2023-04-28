import {Input, Steps} from "antd";
import CIcon from "@coreui/icons-react";
import {cilLockLocked} from "@coreui/icons";
import {CButton} from "@coreui/react";
import React, {useState} from "react";
import postData from "../../../api/backend-api/common/postData";

const CheckPrevPassword = ({current, setCurrent, closeModal, setPrevPassword}) => {
  const [password, setPassword] = useState("");
  const [pwCheck, setPwCheck] = useState(true);

  const onChange = (e) => {
    setPassword(e.target.value);
  }

  const onSubmit = async () => {
    // 비밀번호 일치 여부 boolean
    const res = await postData("/member/password", {password});
    setPwCheck(res);

    if (res) {
      await setCurrent(current + 1);
      await setPrevPassword(password);
      return;
    }

    setPassword('');
  }

  return (
    <>
      {
        pwCheck ? <></> : <span style={{fontSize: "12px"}} className="text-danger">비밀번호를 다시 확인해주세요</span>
      }
      <Input
        placeholder="기존 비밀번호를 입력해주세요"
        type="password"
        value={password}
        prefix={<CIcon icon={cilLockLocked}/>}
        onChange={onChange}
      />
      <div className="float-end mt-4">
        <CButton color="link-secondary" size="sm" onClick={closeModal}>돌아가기</CButton>
        <CButton color="success" size="sm" onClick={onSubmit}>제출하기</CButton>
      </div>
    </>
  )
}

export default CheckPrevPassword;
