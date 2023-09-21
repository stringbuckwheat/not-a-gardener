import GardenCardAnimatedButton from "./GardenCardAnimatedButton";
import React, {useState} from "react";
import {Button, Select, Space} from "antd";
import CIcon from "@coreui/icons-react";
import {cilDrop} from "@coreui/icons";
import postData from "../../../api/backend-api/common/postData";
import getWateringNotificationMsg from "../../../utils/function/getWateringNotificationMsg";
import {useSelector} from "react-redux";

/**
 * not-dry, water, postpone 버튼들 혹은 물주기 폼
 * GardenCard, WaterModalInWaitingList에서 사용
 * @param hovered 이 식물이 hovered 되었는지 (고정으로 띄울 시 true 넘김)
 * @param plantId 해당 식물의 id
 * @param plantName 식물 이름
 * @param chemicalList (물주기 폼에서 쓸) chemicalList
 * @param openNotification action 이후 백엔드에서 받아온 메시지를 띄우는 함수
 * @param updateGardenAfterWatering
 * @param y 애니메이션 y축을 움직일 정도
 * @param wateringCode
 * @param index
 * @param deleteInWaitingListAndTodoList todolist action 이후 콜백함수. todolist, waitinglist에서 삭제
 * @param postponeWatering
 * @param handleWaitingList waitinglist에서의 action 후 콜백 함수. todolist, waitinglist에서 삭제한 후 모달 닫기
 * @returns {JSX.Element}
 * @constructor
 */
const GardenCardAction = ({
                            hovered,
                            plantId,
                            openNotification,
                            updateGardenAfterWatering,
                            y = 5,
                            wateringCode,
                            index,
                            deleteInTodoList,
                            postponeWatering,
                            handleWaitingList,
                            deleteInWaitingListAndTodoList
                          }) => {
  const chemicals = useSelector(state => state.chemicals.forSelect);
  console.log("chemicals", chemicals);

  const [selected, setSelected] = useState("");
  const [chemicalId, setChemicalId] = useState(0);

  // 물을 줬어요 버튼
  const submitWatering = async () => {
    try{
      const data = {
        plantId,
        chemicalId,
        wateringDate: new Date().toISOString().split('T')[0]
      }

      const res = await postData(`/garden/${plantId}/watering`, data);
      console.log("submit watering", res);

      // waitinglist에서의 action 후 콜백 함수. todolist, waitinglist에서 삭제한 후 모달 닫기
      handleWaitingList && handleWaitingList();
      deleteInWaitingListAndTodoList && deleteInWaitingListAndTodoList(plantId);

      // 메시지 띄우기
      const msg = getWateringNotificationMsg(res.wateringMsg.afterWateringCode);
      openNotification(msg);

      // garden 카드 갈아끼우기
      updateGardenAfterWatering && updateGardenAfterWatering(res.gardenResponse);

      setSelected("");
    } catch (e) {
      if(e.code == "B005"){
        alert(e.message);
      }
    }

  }

  if (hovered && selected == "") {
    return <GardenCardAnimatedButton
      setSelected={setSelected}
      y={y}
      plantId={plantId}
      openNotification={openNotification}
      index={index}
      deleteInTodoList={deleteInTodoList}
      postponeWatering={postponeWatering}
      wateringCode={wateringCode}
      handleWaitingList={handleWaitingList}
      deleteInWaitingListAndTodoList={deleteInWaitingListAndTodoList}
    />
  } else if (selected === "watered") {
    return (
      <div className="d-flex justify-content-between">
        <Space className="mb-1">
          <CIcon icon={cilDrop} className="text-info"/>
          <Select options={chemicals} defaultValue={0} style={{width: 120}}
                  onChange={(value) => setChemicalId(value)}/>
          <span>을 줬어요</span>
        </Space>
        <Space>
          <Button onClick={submitWatering} className="bg-info text-white" size="small">제출</Button>
        </Space>
      </div>
    )
  }
}

export default GardenCardAction;
