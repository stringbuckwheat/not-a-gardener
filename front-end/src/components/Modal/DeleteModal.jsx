import React from 'react'
import {useNavigate} from 'react-router-dom';
import {
  CButton,
  CModal,
  CModalBody,
  CModalFooter,
  CModalHeader,
  CModalTitle,
} from '@coreui/react'
import deleteData from 'src/api/backend-api/common/deleteData';
import {useState} from 'react';
import {DeleteOutlined} from '@ant-design/icons';
import {Tooltip} from 'antd';
import isEndWithVowel from "../../utils/function/isEndWithVowel";

const DeleteModal = (props) => {
  const {title, url, path, deleteTooltipMsg, deleteCallBackFunction} = props;

  const button = props.button
    ? props.button
    : <Tooltip title={deleteTooltipMsg}>
      <DeleteOutlined
        className="font-size-18 text-grey"
        onClick={() => {
          setVisible(true)
        }}/>
    </Tooltip>

  const navigate = useNavigate();

  // 삭제 모달 메시지 만들기
  const modalTitleMsg = title + (isEndWithVowel(title) ? '를' : '을');
  const modalBodyMsg = title + (isEndWithVowel(title) ? '는' : '은');

  const remove = () => {
    deleteData(url, path);
    closeDeleteModal();

    if (deleteCallBackFunction) {
      deleteCallBackFunction();
    }

    navigate(url, {replace: true, state: "delete"});
  };

  const [visible, setVisible] = useState(false);
  const closeDeleteModal = () => {
    setVisible(false);
  }

  return (
    <>
      <CModal alignment="center" visible={visible} onClose={closeDeleteModal}>
        <CModalHeader>
          <CModalTitle>{modalTitleMsg} 삭제하실 건가요?</CModalTitle>
        </CModalHeader>
        <CModalBody>삭제한 {modalBodyMsg} 복구할 수 없습니다.</CModalBody>
        <CModalFooter>
          <CButton color="link-secondary" onClick={remove}><small>삭제하기</small></CButton>
          <CButton color="success" onClick={closeDeleteModal}>돌아가기</CButton>
        </CModalFooter>
      </CModal>
      <div onClick={() => {
        setVisible(true)
      }}>
        {button}
      </div>
    </>
  )
}

export default DeleteModal;
