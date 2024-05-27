import {Button, Select, Space} from "antd";
import React, {useState} from "react";
import postData from "../../../../api/backend-api/common/postData";
import getAfterWateringMsg from "../../../../utils/function/getAfterWateringMsg";
import {useDispatch, useSelector} from "react-redux";
import ExceptionCode from "../../../../utils/code/exceptionCode";

const TodoCardWatering = ({plantId, openNotification, setSelected, flipCard}) => {
  // 약품 목록
  const chemicals = useSelector(state => state.chemicals.forSelect);
  const [chemicalId, setChemicalId] = useState(0);

  const dispatch = useDispatch();

  // 물을 줬어요 제출
  const submitWatering = async () => {
    try {
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
      const msg = getAfterWateringMsg(res.wateringMsg.afterWateringCode);
      openNotification(msg);

      // state 정리
      setSelected("");
      flipCard(0);
    } catch (e) {
      if (e.code == ExceptionCode.ALREADY_WATERED) {
        alert(e.message);
      }
    }
  }

  return (
    <div style={{display: "flex", justifyContent: "space-between"}}>
      <Space>
        <span>💧</span>
        <Select options={chemicals} defaultValue={0} style={{width: 100}}
                onChange={(value) => setChemicalId(value)}/>
        <span>을 줬어요</span>
      </Space>
      <Space>
        <Button onClick={submitWatering} style={{backgroundColor: "#007BFF", color: "white"}} size="small">제출</Button>
      </Space>
    </div>
  )
}

export default TodoCardWatering;
