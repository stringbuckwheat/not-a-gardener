import React, {useState} from "react";
import {Input} from "antd";
import {LockOutlined} from "@ant-design/icons";

/**
 * 비밀번호 바꾸기 폼
 * 비밀번호 바꾸기 모달에서 본인 인증 이후 실제로 비밀번호를 바꾸는 부분
 * 부모 컴포넌트: ChangePasswordModal
 * @param current
 * @param setCurrent
 * @param closeModal
 * @param prevPassword
 * @returns {JSX.Element}
 * @constructor
 */
const ChangePasswordForm = ({prevPassword, newPassword, setNewPassword}) => {
  const [confirm, setConfirm] = useState(false);

  const pwRegex = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/;
  const isPasswordValid = pwRegex.test(newPassword) && newPassword !== prevPassword;
  const isValid = isPasswordValid && confirm == newPassword;

  const getConfirmMsg = () => {
    if (!pwRegex.test(newPassword) || newPassword === prevPassword || confirm == "") {
      return "";
    } else if (confirm !== newPassword) {
      return "비밀번호를 다시 확인해주세요"
    } else if (confirm === newPassword) {
      return "비밀번호 확인 완료!"
    }
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
    <div>
      <div style={{marginBottom: "0.8rem"}}>
        <span style={{fontSize: "0.8rem", float: "left", marginBottom: "0.25rem"}}
              className={`text-${isPasswordValid ? "success" : "danger"}`}>
          {getPasswordFeedbackMsg()}
        </span>
        <Input
          placeholder="새 비밀번호를 입력해주세요"
          type="password"
          prefix={<LockOutlined/>}
          onChange={(e) => setNewPassword(e.target.value)}
        />
      </div>
      <div>
        <span style={{fontSize: "0.8rem", float: "left", marginBottom: "0.25rem"}}
              className={`text-${isValid ? "success" : "danger"} float-end mb-1`}>
          {getConfirmMsg()}
        </span>
        <Input
          placeholder="다시 한 번 입력해주세요"
          type="password"
          prefix={<LockOutlined/>}
          onChange={(e) => setConfirm(e.target.value)}
        />

      </div>
    </div>
  )
}

export default ChangePasswordForm;
