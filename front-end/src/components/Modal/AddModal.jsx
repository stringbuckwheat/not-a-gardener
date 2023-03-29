import {CModal, CModalBody, CModalHeader} from "@coreui/react";
import AddPlant from "../../pages/plant/AddPlant";
import getPlantFormArrayWithPlaceName from "../../utils/function/getPlantFormArrayWithPlaceName";

const AddModal = (props) => {
  const {visible, closeModal, callBackFunction, placeNo, placeName} = props;

  return (
    <CModal style={{minWidth: "700px"}} alignment="center" visible={visible} onClose={closeModal}>
      <CModalBody>
        <AddPlant
          itemObjectArray={getPlantFormArrayWithPlaceName(placeName)}
          placeNo={placeNo}
          callBackFunction={callBackFunction}/>
      </CModalBody>
    </CModal>
  )
}

export default AddModal;
