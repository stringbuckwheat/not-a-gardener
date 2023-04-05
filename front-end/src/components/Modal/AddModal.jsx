import {CModal, CModalBody} from "@coreui/react";
import AddPlant from "../../pages/plant/AddPlant";
import getPlantFormArrayWithPlaceName from "../../utils/function/getPlantFormArrayWithPlaceName";

const AddModal = ({visible, closeModal, callBackFunction, placeNo, placeName} ) => {

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
