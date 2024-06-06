import {Button, Modal} from "antd";
import React from "react";
import PlantStatusCode from "../../../../utils/code/plantStatusCode";
import postData from "../../../../api/backend-api/common/postData";
import {useDispatch, useSelector} from "react-redux";
import {useParams} from "react-router-dom";

const HeavyDrinkerModal = ({hideModal}) => {
  const plantId = useParams().plantId;
  const open = useSelector(state => state.plantDetail.etc.heavyDrinkerCheck);

  const dispatch = useDispatch();
  const submit = async () => {
    const data = {
      plantId,
      status: PlantStatusCode.HEAVY_DRINKER.code,
      active: "Y",
      recordedDate: new Date().toISOString().split("T")[0]
    }

    console.log("submit", data);
    const res = await postData(`/plant/${plantId}/status`, data);
    hideModal();
    console.log("res", res);
    dispatch({type: "addStatus", payload: res});
  }

  return (
    <Modal
      open={open}
      title="원래 물을 자주 마시는 식물인가요?"
      onCancel={hideModal}
      footer={[
        <Button key="back" onClick={hideModal}>아니요</Button>,
        <Button key="submit" type="primary" onClick={submit}>네</Button>,
      ]}
    >
      <div>이 식물의 최초 물주기가 3일 이하예요.</div>
      <div style={{marginBottom: "0.5rem"}}>헤비 드링커로 기록하고 물주기 단축에 따른 분갈이 알림을 보내지 않도록 설정할까요?</div>
      <div>(등록일이나 최근 분갈이 기록 기준 1년 뒤 분갈이 알림을 보낼게요)</div>
    </Modal>
  )
}

export default HeavyDrinkerModal;
