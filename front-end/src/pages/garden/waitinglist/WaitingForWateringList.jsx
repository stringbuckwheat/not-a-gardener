import {CCol} from "@coreui/react";
import React, {useState} from "react";
import ClickableTag from "../../../components/tag/basic/ClickableTag";
import WaterModalInWaitingList from "./WaterModalInWaitingList";
import {Tooltip} from "antd";

const WaitingForWateringList = ({waitingList, openNotification, deleteInWaitingListAndTodoList}) => {
  console.log("waitingList", waitingList);
  const [clickedPlant, setClickedPlant] = useState({});
  const onClick = (plant, index) => {
    setClickedPlant({...plant, index});
    setVisible(true);
  }

  // 물주기 모달
  const [visible, setVisible] = useState(false);

  // todolist, waitinglist에서 삭제하고 모달 닫기
  const handleWaitingList = () => {
    deleteInWaitingListAndTodoList(clickedPlant.id);
    setVisible(() => false);
  }

  if (waitingList.length == 0) {
    return;
  }

  return (
    <CCol md={6} xs={12}>
      <Tooltip placement={"topLeft"} title={"물 주기를 2회 이상 기록해주세요"}>
        <div className="small fw-bold text-black">물주기 정보를 기다리고 있는 식물들</div>
      </Tooltip>
      <WaterModalInWaitingList
        visible={visible}
        closeDeleteModal={() => setVisible(false)}
        clickedPlant={clickedPlant}
        openNotification={openNotification}
        handleWaitingList={handleWaitingList}/>
      <div>
        {
          waitingList.map((plant, index) => (
            <ClickableTag
              color="green"
              content={plant.name}
              onClick={() => onClick(plant, index)}
              key={index}/>
          ))
        }
      </div>
    </CCol>
  )
}

export default WaitingForWateringList;
