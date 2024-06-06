import {Button, Modal, Select, Space} from "antd";
import React, {useState} from "react";
import postData from "../../../../api/backend-api/common/postData";
import PlantStatusCode from "../../../../utils/code/plantStatusCode";
import {useDispatch, useSelector} from "react-redux";
import {useParams} from "react-router-dom";
import DateSelector from "../../../../components/form/input/DateSelector";

const StatusModal = ({open, hideModal}) => {
  const plantId = useParams().plantId;
  const status = useSelector(state => state.plantDetail.detail.status);
  const dispatch = useDispatch();

  const [newStatus, setNewStatus] = useState({
    plantId,
    status: PlantStatusCode.ATTENTION_PLEASE.code,
    recordedDate: new Date().toISOString().split("T")[0],
  });
  const onChangeRecordedDate = (date, dateString) => setNewStatus({...newStatus, recordedDate: dateString});

  const submit = async () => {
    console.log("Status Submit", newStatus);

    const res = await postData(`/plant/${plantId}/status`, newStatus);
    console.log("res", res);
    hideModal();
    dispatch({type: "addStatus", payload: res});
  };

  const options = Object.values(PlantStatusCode)
    .filter((value) => value.code !== PlantStatusCode.JUST_REPOTTED.code)
    .map((value) => (
      {value: value.code, label: value.name}
    ));

  return (
    <Modal
      open={open}
      title="식물 상태를 등록/해제할까요?"
      onCancel={hideModal}
      footer={[
        <Button key="back" onClick={hideModal}>취소</Button>,
        <Button key="submit" type="primary" onClick={submit}>제출</Button>,
      ]}
    >
      <div style={{marginBottom: "1rem"}}>
        {status?.map((status) => (
          <div key={status.recordedDate}>{status.recordedDate}~ : {status.status}</div>
        ))}
      </div>

      <div>
        <Select
          style={{width: '30%', marginRight: "1rem"}}
          options={options}
          defaultValue={PlantStatusCode.ATTENTION_PLEASE.code}
          onChange={(status) => setNewStatus({...newStatus, status})}
        />
        <DateSelector
          name={"recordedDate"}
          onChange={onChangeRecordedDate}
          style={{width: '40%'}}/>
      </div>
    </Modal>
  );
};

export default StatusModal;
