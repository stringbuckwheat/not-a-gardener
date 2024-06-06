import {Button, DatePicker, Modal, Radio, Space} from "antd";
import getDisabledDate from "../../../../utils/function/getDisabledDate";
import locale from "antd/es/date-picker/locale/ko_KR";
import React, {useState} from "react";
import dayjs from "dayjs";
import postData from "../../../../api/backend-api/common/postData";
import {useParams} from "react-router-dom";
import {useDispatch} from "react-redux";

const RepotModal = ({open, hideModal}) => {
  const dispatch = useDispatch();
  const plantId = useParams().plantId;
  const [repot, setRepot] = useState({plantId, repotDate: new Date().toISOString(), haveToInitPeriod: "Y", active: "Y"});

  const submit = async () => {
    console.log("submit button clicked", repot);

    if (!repot.repotDate) {
      alert("분갈이 날짜는 비워둘 수 없어요");
    } else if (!repot.haveToInitPeriod) {
      alert("물주기 초기화 여부를 결정해주세요");
    }

    const res = await postData(`/plant/${plantId}/repot`, repot);
    console.log("res", res);
    hideModal();
    dispatch({type: "addStatus", payload: res.status});
  }

  return (
    <Modal
      open={open}
      title="분갈이를 하셨나요?"
      // onOk={handleOk}
      onCancel={hideModal}
      footer={[
        <Button key="back" onClick={hideModal}> 뒤로가기 </Button>,
        <Button key="submit" type="primary" onClick={submit}>등록</Button>,
      ]}
    >
      <div style={{marginBottom: "1.5rem"}}>
        <p>분갈이 날짜를 입력해주세요</p>
        <DatePicker
          name="wateringDate"
          style={{width: "100%",}}
          disabledDate={getDisabledDate}
          format={"YYYY-MM-DD"}
          defaultValue={dayjs(new Date().toISOString().slice(0, 10), 'YYYY-MM-DD')}
          onChange={(date, dateString) => setRepot(() => ({...repot, repotDate: dateString}))}
          locale={locale}/>
      </div>

      <div>
        <p>물주기 간격을 초기화할까요?</p>
        <Radio.Group
          onChange={(e) => setRepot({...repot, haveToInitPeriod: e.target.value})}
          value={repot.haveToInitPeriod}>
          <Space direction="vertical">
            <Radio value={"Y"}>네</Radio>
            <Radio value={"N"}>아니요</Radio>
          </Space>
        </Radio.Group>
      </div>
    </Modal>
  )
}

export default RepotModal;
