import {Button, Select, Space} from "antd";
import React, {useState} from "react";
import postData from "../../../../api/backend-api/common/postData";
import getWateringNotificationMsg from "../../../../utils/function/getWateringNotificationMsg";
import {useDispatch, useSelector} from "react-redux";

const TodoCardWatering = ({plantId, openNotification, setSelected, flipCard}) => {
  // 약품 목록
  const chemicals = useSelector(state => state.chemicals.forSelect);
  const [chemicalId, setChemicalId] = useState(0);

  const dispatch = useDispatch();

  // 물을 줬어요 제출
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
      dispatch({type: 'deleteInTodoList', payload: plantId});

      // 메시지 띄우기
      const msg = getWateringNotificationMsg(res.wateringMsg.afterWateringCode);
      openNotification(msg);

      // state 정리
      setSelected("");
      flipCard(0);
    } catch (e) {
      if(e.code == "B005"){
        alert(e.message);
      }
    }
  }

  return (
    <div className="d-flex justify-content-between">
      <Space className="mb-1">
        <span>💧</span>
        <Select options={chemicals} defaultValue={0} style={{width: 100}}
                onChange={(value) => setChemicalId(value)}/>
        <span>을 줬어요</span>
      </Space>
      <Space>
        <Button onClick={submitWatering} className="bg-info text-white" size="small">제출</Button>
      </Space>
    </div>
  )
}

export default TodoCardWatering;
