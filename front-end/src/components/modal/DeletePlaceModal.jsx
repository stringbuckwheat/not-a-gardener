import {DeleteOutlined} from "@ant-design/icons";
import React, {useState} from "react";
import deleteData from "../../api/backend-api/common/deleteData";
import {useNavigate} from "react-router-dom";
import {Button, Modal} from "antd";

const DeletePlaceModal = ({plantListSize, placeId}) => {
  const hasNoPlant = plantListSize == 0;

  const navigate = useNavigate();
  const [visible, setVisible] = useState(false);
  const closeDeleteModal = () => setVisible(false);

  const remove = async () => {
    console.log("remove", placeId);
    await deleteData(`/place/${placeId}`);
    navigate("/place", {replace: true});
  }

  const [deleteWithAllPlant, setDeleteWithAllPlant] = useState(false);
  const getMessage = () => {
    if (hasNoPlant) {
      return "삭제한 장소는 복구할 수 없어요.";
    } else if (!hasNoPlant && !deleteWithAllPlant) {
      return "체크박스를 누르면 여러 식물을 한 번에 이동할 수 있어요";
    } else if (!hasNoPlant && deleteWithAllPlant) {
      return "이 장소에 속한 모든 식물과 루틴 등이 모두 함께 삭제됩니다"
    }
  }

  const footer = <>
    <Button type="text" onClick={hasNoPlant || deleteWithAllPlant ? remove : () => setDeleteWithAllPlant(true)}>
      <small>{hasNoPlant ? "삭제하기" : deleteWithAllPlant ? "그래도 삭제할래요" : "이 장소의 모든 식물을 삭제하고 장소도 삭제하기"}</small>
    </Button>
    <Button onClick={closeDeleteModal}>돌아가기</Button>
  </>

  return (
    <>
      <DeleteOutlined
        style={{fontSize: "1.2rem", color: "grey"}}
        onClick={() => setVisible(true)}
      />
      <Modal open={visible} onClose={closeDeleteModal} footer={footer}>
        <h3>{hasNoPlant ? "이 장소를 삭제하실건가요?" : deleteWithAllPlant ? "한번 더 확인해주세요" : "이 장소의 식물을 삭제/이동해주세요"}</h3>
        <div style={{fontSize: "1rem"}}>{getMessage()}</div>
      </Modal>
    </>
  )
}

export default DeletePlaceModal;
