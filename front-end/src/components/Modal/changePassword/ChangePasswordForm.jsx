import React, {useState} from "react";
import {Input} from "antd";
import CIcon from "@coreui/icons-react";
import {cilLockLocked} from "@coreui/icons";
import {CButton} from "@coreui/react";
import updateData from "../../../api/backend-api/common/updateData";

const ChangePasswordForm = (props) => {
  const {current, setCurrent, closeModal, prevPassword} = props;
  const [newPassword, setNewPassword] = useState("");
  const [confirm, setConfirm] = useState(false);
  const pwRegex = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/;
  const isPasswordValid = pwRegex.test(newPassword) && newPassword !== prevPassword;
  const isValid = isPasswordValid && confirm == newPassword;


  const onChange = (e) => {
    setNewPassword(e.target.value);
  }

  const confirmPassword = (e) => {
    setConfirm(e.target.value);
  }

  const getConfirmMsg = () => {
    if(!pwRegex.test(newPassword) || newPassword === prevPassword || confirm == ""){
      return "";
    } else if(confirm !== newPassword){
      return "비밀번호를 다시 확인해주세요"
    } else if(confirm === newPassword){
      return "비밀번호 확인 완료!"
    }
  }

  const onSubmit = async () => {
    console.log("onsubmit");
    await updateData("/member", "pw", {pw: newPassword});
    setCurrent(current + 1);
  }


  const getPasswordFeedbackMsg = () => {
    let msg = "숫자, 특수문자를 포함하여 8자리 이상이어야 해요."; // regex 통과 못함

    if (newPassword === prevPassword) {
      msg = "이전과 동일한 비밀번호는 사용할 수 없어요";
    } else if (isPasswordValid) {
      msg = "좋아요!";
    }

    return msg;
  }


  return (
    <>
      <div>
      <Input
        placeholder="새 비밀번호를 입력해주세요"
        type="password"
        prefix={<CIcon icon={cilLockLocked}/>}
        onChange={onChange}
      />
      <span
        style={{fontSize: "12px"}}
        className={`text-${isPasswordValid ? "success" : "danger"} float-end mb-1`}>
        {getPasswordFeedbackMsg()}
      </span>
      <Input
        placeholder="다시 한 번 입력해주세요"
        type="password"
        prefix={<CIcon icon={cilLockLocked}/>}
        onChange={confirmPassword}
      />
      <span
        style={{fontSize: "12px"}}
        className={`text-${isValid ? "success" : "danger"} float-end mb-1`}>
        {getConfirmMsg()}
      </span>
      </div>
      <div className="float-end mt-4">
        <CButton color="link-secondary" size="sm" onClick={closeModal}>돌아가기</CButton>
        <CButton
          color={isValid ? "success" : "light"}
          disabled={!(isValid)}
          size="sm"
          onClick={onSubmit}>
          제출하기
        </CButton>
      </div>
    </>
  )
}

export default ChangePasswordForm;
