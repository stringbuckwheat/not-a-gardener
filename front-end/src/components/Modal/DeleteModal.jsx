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
import authAxios from '../../utils/requestInterceptor';

const DeleteModal = (props) => {
    const {visible, title} = props;
    
    const navigate = useNavigate();

    const remove = () => {
        authAxios.delete(props.url + "/" + props.path)
        .then(() => {
          navigate(props.url);
        })
    };

    return (
        <>
          <CModal alignment="center" visible={visible} onClose={props.closeDeleteModal}>
            <CModalHeader>
              <CModalTitle>이 {title}를 삭제하실 건가요?</CModalTitle>
            </CModalHeader>
            <CModalBody>삭제한 {title}는 복구할 수 없습니다.</CModalBody>
            <CModalFooter>
            <CButton color="link-secondary" onClick={remove}><small>삭제하기</small></CButton>
              <CButton color="success" onClick={props.closeDeleteModal}>돌아가기</CButton>
            </CModalFooter>
          </CModal>
        </>
      )
}

export default DeleteModal;