import {CModal, CModalBody, CModalHeader} from "@coreui/react";
import AddPlant from "../../pages/plant/AddPlant";

const AddModal = (props) => {
  const {visible, placeList, closeModal, callBackFunction} = props;

  return (
    <CModal style={{minWidth: "700px"}} alignment="center" visible={visible} onClose={closeModal}>
      <CModalBody>
        <AddPlant
          placeList={placeList}
          callBackFunction={callBackFunction}/>
      </CModalBody>
    </CModal>
  )
}

export default AddModal;
