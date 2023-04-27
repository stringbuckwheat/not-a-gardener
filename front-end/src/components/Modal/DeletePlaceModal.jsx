import {CButton, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle} from "@coreui/react";
import {DeleteOutlined} from "@ant-design/icons";
import React, {useState} from "react";
import deleteData from "../../api/backend-api/common/deleteData";
import {useNavigate} from "react-router-dom";

const DeletePlaceModal = ({plantListSize, placeNo}) => {
  const hasNoPlant = plantListSize == 0;

  const navigate = useNavigate();

  const [visible, setVisible] = useState(false);
  const closeDeleteModal = () => {
    setVisible(false);
  }

  const remove = async () => {
    await deleteData(`/place/${placeNo}`);
    navigate("/place", {replace: true});
  }

  const [deleteWithAllPlant, setDeleteWithAllPlant] = useState(false);
  const getMessage = () => {
    if(hasNoPlant){
      return "삭제한 장소는 복구할 수 없어요.";
    } else if(!hasNoPlant && !deleteWithAllPlant){
      return "체크박스를 누르면 여러 식물을 한 번에 이동할 수 있어요";
    } else if(!hasNoPlant && deleteWithAllPlant){
      return "이 장소에 속한 모든 식물과 루틴 등이 모두 함께 삭제됩니다"
    }
  }

  return (
    <>
      <CModal alignment="center" visible={visible} onClose={closeDeleteModal}>
        <CModalHeader>
          <CModalTitle>{hasNoPlant ? "이 장소를 삭제하실건가요?" : deleteWithAllPlant ? "한번 더 확인해주세요" : "이 장소의 식물을 삭제/이동해주세요"}</CModalTitle>
        </CModalHeader>
        <CModalBody>{getMessage()}</CModalBody>
        <CModalFooter>
          <CButton color="link-secondary" size="sm"
                   onClick={hasNoPlant || deleteWithAllPlant ? remove : () => setDeleteWithAllPlant(true)}>
            <small>{hasNoPlant ? "삭제하기" : deleteWithAllPlant ? "그래도 삭제할래요" : "이 장소의 모든 식물을 삭제하고 장소도 삭제하기"}</small>
          </CButton>
          <CButton color="success" size="sm" onClick={closeDeleteModal}>돌아가기</CButton>
        </CModalFooter>
      </CModal>

      <DeleteOutlined
        className="font-size-18 text-grey"
        onClick={() => {
          setVisible(true)
        }}/>
    </>
  )
}

export default DeletePlaceModal;
