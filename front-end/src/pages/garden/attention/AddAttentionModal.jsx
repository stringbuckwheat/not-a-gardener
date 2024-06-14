import {Button, Col, ConfigProvider, Modal, Row, Select} from "antd";
import themeGreen from "../../../theme/themeGreen";
import PlantStatusCode from "../../../utils/code/plantStatusCode";
import getData from "../../../api/backend-api/common/getData";
import React, {useEffect, useState} from "react";
import DateSelector from "../../../components/form/input/DateSelector";
import postData from "../../../api/backend-api/common/postData";
import {useDispatch} from "react-redux";
import GardenAction from "../../../redux/reducer/garden/gardenAction";

const AddAttentionModal = ({open, closeModal}) => {
  const dispatch = useDispatch();
  const [plants, setPlants] = useState([]);
  const [attention, setAttention] = useState({
    plantId: 0,
    statusType: PlantStatusCode.ATTENTION_PLEASE.code,
    recordedDate: new Date().toISOString().split("T")[0],
    active: "Y"
  });

  const onMountModal = async () => {
    const res = await getData("/plant/non-attention");
    console.log("ADD ATTENTION res", res);

    const plantsForSelect = res.map((plant) => ({label: plant.name, value: plant.id}));
    setPlants(plantsForSelect);
  }

  useEffect(() => {
    onMountModal();
  }, []);

  useEffect(() => {
    if (plants.length > 0) {
      setAttention(prevAttention => ({...prevAttention, plantId: plants[0].value}));
    }
  }, [plants]);

  const submit = async () => {
    console.log("submit req", attention);

    const res = await postData(`/status`, attention);
    console.log("submit res", res);

    closeModal();
    dispatch({type: GardenAction.ADD_ATTENTION, payload: res});
  }

  const titleStyle = {marginBottom: "2rem", marginTop: "1rem", display: "flex"};

  return (
    <ConfigProvider theme={themeGreen}>
      <Modal open={open} onClose={closeModal} closable={false}
             footer={
               <>
                 <Button type={"primary"} onClick={submit}>제출</Button>
                 <Button onClick={closeModal}>취소</Button>
               </>}
      >
        <h5 style={titleStyle}>요주의 식물 추가</h5>
        <Row gutter={[16, 16]}>
          <Col xs={24} md={12}>
            <Select
              className="width-full"
              value={attention.plantId}
              style={{width: "95%"}}
              onChange={(value) => setAttention(prevAttention => ({...prevAttention, plantId: value}))}
              options={plants}
              name="plantIds"
            />
          </Col>
          <Col xs={24} md={12}>
            <DateSelector
              name="recordedDate"
              onChange={(date, dateString) => setAttention(prevAttention => ({...prevAttention, recordedDate: dateString}))}
              style={{width: '95%'}}
            />
          </Col>
        </Row>
      </Modal>
    </ConfigProvider>
  )
}

export default AddAttentionModal
