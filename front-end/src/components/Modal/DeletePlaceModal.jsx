import {CButton, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle} from "@coreui/react";
import {DeleteOutlined} from "@ant-design/icons";
import React, {useState} from "react";
import deleteData from "../../api/backend-api/common/deleteData";
import {useNavigate} from "react-router-dom";

const DeletePlaceModal = (props) => {
  const {plantListSize, placeNo} = props;

  const hasNoPlant = plantListSize == 0;

  const navigate = useNavigate();

  const [visible, setVisible] = useState(false);
  const closeDeleteModal = () => {
    setVisible(false);
  }

  const remove = () => {
    console.log("placeNo", placeNo);
    deleteData("/place", placeNo);
    navigate("/place", {replace: true});
  }

  return (
    <>
      <CModal alignment="center" visible={visible} onClose={closeDeleteModal}>
        <CModalHeader>
          <CModalTitle>{hasNoPlant ? "이 장소를 삭제하실건가요?" : "이 장소의 식물을 삭제/이동해주세요"}</CModalTitle>
        </CModalHeader>
        <CModalBody>{hasNoPlant ? "삭제한 장소는 복구할 수 없어요." : "체크박스를 누르면 여러 식물을 한 번에 이동할 수 있어요"}</CModalBody>
        <CModalFooter>
          <CButton color="link-secondary"
                   onClick={remove}><small>{hasNoPlant ? "삭제하기" : "이 장소의 모든 식물을 삭제하고 장소도 삭제하기"}</small></CButton>
          <CButton color="success" onClick={closeDeleteModal}>돌아가기</CButton>
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
