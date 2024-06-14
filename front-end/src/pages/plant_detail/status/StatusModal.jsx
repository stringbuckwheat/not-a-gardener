import {Button, Modal, Select, Row, Col} from "antd";
import React, {useEffect, useState} from "react";
import postData from "../../../api/backend-api/common/postData";
import PlantStatusCode from "../../../utils/code/plantStatusCode";
import {useDispatch, useSelector} from "react-redux";
import {useParams} from "react-router-dom";
import DateSelector from "../../../components/form/input/DateSelector";
import PlantStatusTags from "../../../components/etc/PlantStatusTags";
import PlantDetailAction from "../../../redux/reducer/plant_detail/plantDetailAction";

const StatusModal = ({open, hideModal}) => {
  const plantId = useParams().plantId;
  const status = useSelector(state => state.plantDetail.detail.status);
  const dispatch = useDispatch();

  const [newStatus, setNewStatus] = useState({
    plantId,
    statusType: PlantStatusCode.ATTENTION_PLEASE.code,
    recordedDate: new Date().toISOString().split("T")[0],
    active: "Y"
  });

  useEffect(() => {
    setNewStatus(prevStatus => ({
      ...prevStatus,
      active: status[PlantStatusCode[prevStatus.statusType].label] === "Y" ? "N" : "Y"
    }));
  }, [status]);

  const onChangeRecordedDate = (date, dateString) => setNewStatus({...newStatus, recordedDate: dateString});

  const submit = async () => {
    const active = status[PlantStatusCode[newStatus.statusType].label] == "Y" ? "N" : "Y";
    const res = await postData(`/plant/${plantId}/status`, {...newStatus, active});
    console.log("submit res", res);

    hideModal();

    dispatch({type: PlantDetailAction.FETCH_ACTIVE_STATUS, payload: res.status});
    dispatch({type: PlantDetailAction.ADD_STATUS_LOG, payload: res.statusLog});
  };

  const options = Object.values(PlantStatusCode)
    .filter((value) => value.code !== PlantStatusCode.JUST_REPOTTED.code)
    .map((value) => (
      {value: value.code, label: value.name}
    ));

  return (
    <Modal
      open={open}
      title="식물 상태를 설정/해제할까요?"
      onCancel={hideModal}
      footer={null}
    >
      <div style={{marginBottom: '2rem'}}>
        <PlantStatusTags status={status}/>
      </div>
      <Row gutter={[16, 16]}>
        <Col xs={24} md={12}>
          <Select
            options={options}
            defaultValue={PlantStatusCode.ATTENTION_PLEASE.code}
            onChange={(statusType) => setNewStatus({
              ...newStatus,
              statusType,
              active: status[PlantStatusCode[statusType].label] == "Y" ? "N" : "Y"
            })}
            style={{width: '100%'}}
          />
        </Col>
        <Col xs={24} md={12}>
          <DateSelector
            name="recordedDate"
            onChange={onChangeRecordedDate}
            style={{width: '100%'}}
          />
        </Col>
      </Row>
      <Row justify="end" style={{marginTop: '1rem'}}>
        <Button type="primary" onClick={submit}>
          {newStatus.active == 'Y' ? '설정' : '해제'}
        </Button>
      </Row>
    </Modal>
  );
};

export default StatusModal;
