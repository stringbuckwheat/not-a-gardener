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
import LogOut from 'src/utils/logout'


/*
사용 예시
<DeleteModal 
    visible={visible} 
    deleteItem={state.placeNo} 
    closeDeleteModal={closeDeleteModal} 
    title={"장소"}
    deleteUrl={"/place/" + state.placeNo}
    callBackFunction={callBackFunction}/>

- 부모 컴포넌트에서 만들 것: visible(state), closeDeleteModal(함수)
*/

const DeleteModal = (props) => {
    const {visible, title, deleteItem, deleteUrl} = props;

    console.log("delete modal");
    console.log("visible", visible);
    console.log("title", title);
    console.log("deleteItem", deleteItem);
    console.log("deleteUrl", deleteUrl);
    
    const remove = () => {
        authAxios.delete(deleteUrl)
        .then(props.callBackFunction())
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