import React, { useState, useEffect }  from 'react'
import {
  CButton,
  CModal,
  CModalBody,
  CModalFooter,
  CModalHeader,
  CModalTitle,
} from '@coreui/react'
import authAxios from '../../utils/requestInterceptor'
import { useNavigate } from 'react-router-dom';

const DeletePlant = (props) => {
  console.log("식물 삭제 모달")

  // props.closeModal로 부모 컴포넌트의 state 변경 함수 넘겨줌
  const { deleteVisible, clickedPlant } = props;
  console.log("props", props)

  const onClick = () => {
    authAxios.delete('/garden/plant/' + props.clickedPlant)
      .then((res) => {
        props.closeDeleteModal();
        props.onRemove();
      })
  }

    return (
        <CModal visible={props.deleteVisible} onClose={props.closeDeleteModal}>
          <CModalHeader>
            <CModalTitle>이 식물을 삭제하나요?</CModalTitle>
          </CModalHeader>
          <CModalBody>삭제한 식물은 복구할 수 없습니다.</CModalBody>
          <CModalFooter>
            <CButton color="primary" type="button" onClick={onClick}>
              삭제할게요
            </CButton>
            <CButton color="warning" type="button">
              뒤로 갈래요
            </CButton>
          </CModalFooter>
        </CModal>
        )
}

export default DeletePlant
