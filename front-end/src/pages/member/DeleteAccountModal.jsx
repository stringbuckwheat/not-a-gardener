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

const DeleteAccountModal = (props) => {
    const {visible, memberNo} = props;

    const navigate = useNavigate();
    
    const deleteAccount = () => {
        authAxios.delete("/member/" + memberNo)
        .then(() => {
            localStorage.clear();
            navigate("/login");
        })
    }

    return (
        <>
          <CModal alignment="center" visible={visible} onClose={props.closeDeleteModal}>
            <CModalHeader>
              <CModalTitle>계정을 삭제하실 건가요?</CModalTitle>
            </CModalHeader>
            <CModalBody>삭제된 계정과 데이터는 복구가 불가능합니다.</CModalBody>
            <CModalFooter>
            <CButton color="link-secondary" onClick={deleteAccount}><small>탈퇴하기</small></CButton>
              <CButton color="success" onClick={props.closeDeleteModal}>돌아가기</CButton>
            </CModalFooter>
          </CModal>
        </>
      )
}

export default DeleteAccountModal;