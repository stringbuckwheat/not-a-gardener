import React from 'react'
import {useNavigate} from 'react-router-dom';
import deleteData from 'src/api/backend-api/common/deleteData';
import {useState} from 'react';
import {DeleteOutlined} from '@ant-design/icons';
import {Button, Modal, Tooltip} from 'antd';
import isEndWithVowel from "../../utils/function/isEndWithVowel";

// url, path 분리해놔야 navigate으로 쓰기 편함
const DeleteModal = ({title, url, path, deleteTooltipMsg, deleteCallBackFunction, button, detailMsg}) => {

  const deleteVisibleButton = button
    ? button
    : <Tooltip title={deleteTooltipMsg}>
      <DeleteOutlined
        style={{fontSize: "1.2rem", color: "grey"}}
        onClick={() => {
          setVisible(true)
        }}/>
    </Tooltip>

  const navigate = useNavigate();

  // 삭제 모달 메시지 만들기
  const modalTitleMsg = title + (isEndWithVowel(title) ? '를' : '을');
  const modalBodyMsg = title + (isEndWithVowel(title) ? '는' : '은');

  const remove = async () => {
    await deleteData(`${url}/${path}`);
    closeDeleteModal();

    deleteCallBackFunction && deleteCallBackFunction();

    navigate(url, {replace: true, state: "delete"});
  };

  const [visible, setVisible] = useState(false);
  const closeDeleteModal = () => setVisible(false);

  const footer = <>
    <Button type={"text"} onClick={remove}><small>삭제하기</small></Button>
    <Button type="primary" onClick={closeDeleteModal}>돌아가기</Button>
  </>

  return (
    <>
      <div onClick={() => setVisible(true)}>
        {deleteVisibleButton}
      </div>
      <Modal open={visible}
             onClose={closeDeleteModal}
             closable={false}
             footer={footer}>
        <h3 style={{marginBottom: "1rem"}}>{modalTitleMsg} 삭제하실 건가요?</h3>
        <div>삭제한 {modalBodyMsg} 복구할 수 없습니다.</div>
        {
          detailMsg ? <div>{detailMsg}</div> : <></>
        }
      </Modal>
    </>
  )
}

export default DeleteModal;
