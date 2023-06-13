import React, {useState} from "react";
import {
  CButton,
  CCol,
  CModal,
  CModalBody,
  CRow
} from "@coreui/react";
import {Steps} from "antd";
import CheckPrevPassword from "./changePassword/CheckPrevPassword";
import ChangePasswordForm from "./changePassword/ChangePasswordForm";

const ChangePasswordModal = () => {
  const [visible, setVisible] = useState(false);
  const [current, setCurrent] = useState(0);
  const [prevPassword, setPrevPassword] = useState("");

  const closeModal = () => {
    setCurrent(0);
    setVisible(false);
  }

  const steps = [{title: '기존 비밀번호 확인'}, {title: '비밀번호 변경'}, {title: '완료'}]

  return (
    <>
      <CModal alignment="center" visible={visible} onClose={closeModal}>
        <CModalBody>
          <CRow className="justify-content-center">
            <CCol md={10}>
              <h5 className="mt-2">비밀번호 변경</h5>
              <Steps
                className="mt-3 mb-4"
                size="small"
                current={current}
                items={steps}
              />
              {
                current === 0
                  ? <CheckPrevPassword
                    current={current}
                    setCurrent={setCurrent}
                    closeModal={closeModal}
                    setPrevPassword={setPrevPassword}
                  />
                  : current === 1
                    ? <ChangePasswordForm
                      current={current}
                      setCurrent={setCurrent}
                      closeModal={closeModal}
                      prevPassword={prevPassword}
                    />
                    : <h6 className="text-success">비밀번호 변경이 완료되었어요!</h6>
              }
            </CCol>
          </CRow>
        </CModalBody>
      </CModal>

      <CButton
        size="sm"
        onClick={() => setVisible(true)}
        color="success"
        variant="outline">
        비밀번호 변경
      </CButton>
    </>
  )
}

export default ChangePasswordModal;
