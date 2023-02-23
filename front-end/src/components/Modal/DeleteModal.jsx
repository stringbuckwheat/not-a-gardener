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

/*
<DeleteModal 
    visible={visible} 
    deleteItem={state.placeNo} 
    closeDeleteModal={closeDeleteModal} 
    title={"장소"}
    deleteUrl={"/place/" + state.placeNo}/>
*/

const DeleteModal = (props) => {
    const {visible, title, deleteItem, deleteUrl} = props;

    console.log("delete modal");
    console.log("visible", visible);
    console.log("title", title);
    console.log("deleteItem", deleteItem);
    console.log("deleteUrl", deleteUrl);

    const navigate = useNavigate();
    
    const deleteAccount = () => {
        authAxios.delete(deleteUrl)
        .then(() => {
            localStorage.clear();
            navigate("/login");
        })
    }

    return (
        <>
          <CModal alignment="center" visible={visible} onClose={props.closeDeleteModal}>
            <CModalHeader>
              <CModalTitle>{title}를 삭제하실 건가요?</CModalTitle>
            </CModalHeader>
            <CModalBody>삭제된 {title}는 복구가 불가능합니다.</CModalBody>
            <CModalFooter>
            <CButton color="link-secondary" onClick={deleteAccount}><small>삭제하기</small></CButton>
              <CButton color="success" onClick={props.closeDeleteModal}>돌아가기</CButton>
            </CModalFooter>
          </CModal>
        </>
      )
}

export default DeleteModal;