import {DeleteOutlined} from "@ant-design/icons";
import React, {useState} from "react";
import {CButton, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle} from "@coreui/react";

const RemoveModal = ({remove, modalTitle, modalContent, deleteButtonTitle}) => {
  const [visible, setVisible] = useState(false);
  const closeDeleteModal = () => setVisible(false);

  return (
    <>
      <CModal alignment="center" visible={visible} onClose={closeDeleteModal}>
        <CModalHeader>
          <CModalTitle>{modalTitle}</CModalTitle>
        </CModalHeader>
        <CModalBody>{modalContent}</CModalBody>
        <CModalFooter>
          <CButton
            color="link-secondary"
            size="sm"
            onClick={remove}>
            <small>{deleteButtonTitle}</small>
          </CButton>
          <CButton color="success" size="sm" onClick={closeDeleteModal}>돌아가기</CButton>
        </CModalFooter>
      </CModal>

      <DeleteOutlined
        className="font-size-18 text-grey"
        onClick={() =>
          setVisible(true)
        }/>
    </>
  )
}

export default RemoveModal
