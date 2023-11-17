import React, {useState} from "react";
import {Button, Modal, Steps} from "antd";
import CheckPrevPassword from "./changePassword/CheckPrevPassword";
import ChangePasswordForm from "./changePassword/ChangePasswordForm";
import postData from "../../api/backend-api/common/postData";
import updateData from "../../api/backend-api/common/updateData";

const ChangePasswordModal = () => {
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState(0);

  const closeModal = () => {
    setCurrent(0);
    setPassword("");
    setNewPassword("");
    setVisible(false);
  }

  const steps = [{title: '기존 비밀번호 확인'}, {title: '비밀번호 변경'}, {title: '완료'}]

  ///// CheckPrevPassword
  const [password, setPassword] = useState("");
  const [pwCheck, setPwCheck] = useState(true);

  const onChange = (e) => {
    setPassword(e.target.value);
  }

  const checkPrevPassword = async () => {
    // 비밀번호 일치 여부 boolean
    const res = await postData("/gardener/password", {password});
    setPwCheck(res);

    if (res) {
      setCurrent(current + 1);
      return;
    }

    setPassword('');
  }

  ///// ChangePassword
  const [newPassword, setNewPassword] = useState("");

  const changePassword = async () => {
    await updateData("/gardener/password", {password: newPassword});
    setCurrent(current + 1);

    console.log("current", current);
  }

  const getProps = () => {
    const common = {
      open: visible,
      onClose: closeModal,
      closable: false
    }

    if (current === 0 || current === 1) {
      return {
        ...common,
        onOk: current === 0 ? checkPrevPassword : current === 1 ? changePassword : closeModal,
        onCancel: closeModal,
        okText: "제출하기",
        cancelText: "돌아가기"
      }
    } else {
      return {
        ...common,
        footer: <Button type={"primary"} onClick={closeModal}>돌아가기</Button>
      }
    }
  }


  return (
    <>
      <Button
        onClick={() => setVisible(true)}
        style={{marginLeft: "0.5rem"}}
      >
        비밀번호 변경
      </Button>

      <Modal {...getProps()}>
        <h5 style={{marginTop: "1rem"}}>비밀번호 변경</h5>
        <Steps
          style={{marginTop: "1.5rem", marginBottom: "2rem"}}
          size="small"
          current={current}
          items={steps}
        />
        {
          current === 0
            ? <CheckPrevPassword
              pwCheck={pwCheck}
              password={password}
              onChange={onChange}
            />
            : current === 1
              ? <ChangePasswordForm
                prevPassword={password}
                newPassword={newPassword}
                setNewPassword={setNewPassword}
              />
              : <h6 style={{color: "green"}}>비밀번호 변경이 완료되었어요!</h6>
        }
      </Modal>
    </>
  )
}

export default ChangePasswordModal;
