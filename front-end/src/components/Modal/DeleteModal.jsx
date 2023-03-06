import React from 'react'
import { useNavigate } from 'react-router-dom';
import {
  CButton,
  CModal,
  CModalBody,
  CModalFooter,
  CModalHeader,
  CModalTitle,
} from '@coreui/react'
import authAxios from '../../utils/interceptors';

const DeleteModal = (props) => {
  const { visible, title, url, path, closeDeleteModal } = props;

  const navigate = useNavigate();

  // 은/는, 을/를 구분 함수...
  const isEndWithVowel = (title) => {
    // 마지막 단어의 char code 반환
    const finalCharCode = title.charCodeAt(title.length - 1);

    // 0이면 받침 없고 아니면 받침 있음
    const tmp = (finalCharCode - 44032) % 28;
    return tmp == 0;
  }

  const modalTitleMsg = title + (isEndWithVowel(title) ? '를' : '을');
  const modalBodyMsg = title + (isEndWithVowel(title) ? '는' : '은');

  const remove = () => {
    authAxios.delete(url + "/" + path)
      .then(() => {
        navigate(url);
      })
  };

  return (
    <>
      <CModal alignment="center" visible={visible} onClose={props.closeDeleteModal}>
        <CModalHeader>
          <CModalTitle>이 {modalTitleMsg} 삭제하실 건가요?</CModalTitle>
        </CModalHeader>
        <CModalBody>삭제한 {modalBodyMsg} 복구할 수 없습니다.</CModalBody>
        <CModalFooter>
          <CButton color="link-secondary" onClick={remove}><small>삭제하기</small></CButton>
          <CButton color="success" onClick={closeDeleteModal}>돌아가기</CButton>
        </CModalFooter>
      </CModal>
    </>
  )
}

export default DeleteModal;
