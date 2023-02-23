import React, { useState, useEffect }  from 'react'
import {
  CButton,
  CModal,
  CModalBody,
  CModalFooter,
  CModalHeader,
  CModalTitle,
} from '@coreui/react'
import authAxios from '../../utils/requestInterceptor';

const WaterModal = (props) => {
  console.log("water modal");

  // props.closeModal로 부모 컴포넌트의 state 변경 함수 넘겨줌
  const { visible, clickedPlant } = props;
  console.log("clickedPlant", clickedPlant);
  const [fertilized, setFertilized] = useState("N");

  const onClick = () => {
    setFertilized("Y");
    submit();
  }

  const submit = () => {
    console.log("water submit")

    authAxios.post("/garden/water", {plantNo: clickedPlant, fertilized: fertilized})
    .then((res) => {
      console.log("res", res);
      props.closeModal();
    })
    .catch(error => console.log(error));
  }

    return (
        <CModal visible={visible} onClose={props.closeModal}>
          <CModalHeader>
            <CModalTitle>물을 주셨나요?</CModalTitle>
          </CModalHeader>
          <CModalFooter>
            <CButton color="primary" type="button" onClick={submit}>
              네, 물을 줬어요 💧
            </CButton>
            <CButton color="warning" type="button" onClick={onClick}>
              비료를 줬어요 🍗
            </CButton>
          </CModalFooter>
        </CModal>
        )
}

export default WaterModal