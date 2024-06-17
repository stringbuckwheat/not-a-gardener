import React, {useState} from 'react';
import {Button, Card, Select, Space} from "antd";
import {useDispatch, useSelector} from "react-redux";
import {useParams} from "react-router-dom";
import DateSelector from "../../../components/form/input/DateSelector";
import postData from 'src/api/backend-api/common/postData';
import ExceptionCode from "../../../utils/code/exceptionCode";
import './WateringForm.scss';
import PlantDetailAction from "../../../redux/reducer/plant_detail/plantDetailAction";
import WateringAction from "../../../redux/reducer/waterings/wateringAction";

const WateringForm = ({wateringCallBack}) => {
  const plantId = useParams().plantId;
  const page = useSelector(state => state.plantDetail.etc.page);
  const chemicalList = useSelector(state => state.plantDetail.chemicals);

  const [watering, setWatering] = useState({
    plantId,
    chemicalId: chemicalList[0].value,
    wateringDate: new Date().toISOString().split("T")[0]
  });
  const dispatch = useDispatch();
  const closeForm = () => dispatch({type: PlantDetailAction.SET_WATERING_FORM_OPENED, payload: false});

  const onSubmit = async () => {
    console.log("submit data", watering);
    closeForm();

    const res = await postData(`/plant/${plantId}/watering?page=${page - 1}`, watering);
    console.log("watering form add res", res);
    dispatch({type: WateringAction.ADD_TOTAL_WATERING, payload: null});
    wateringCallBack(res);
  }

  const onChangeWateringDate = (date, dateString) => setWatering(() => ({...watering, wateringDate: dateString}));

  return (
    <div className="watering-form-container">
      <Card
        className="watering-form-card"
        style={{borderColor: "green"}}
        onClose={closeForm}>
        <h5>물 주기</h5>

        <div>
          <small>언제 주었나요?</small>
        </div>
        <DateSelector
          name={"recordedDate"}
          onChange={onChangeWateringDate}
        />

        <div>
          <small>무엇을 주었나요?</small>
        </div>
        <Select
          className="width-full"
          defaultValue="맹물"
          style={{width: "100%",}}
          onChange={(value) => setWatering(() => ({...watering, chemicalId: value}))}
          options={chemicalList}
          name="chemicalId"
        />
        <div style={{marginTop: "1rem", display: "flex", justifyContent: "flex-end"}}>
          <Space>
            <Button onClick={closeForm} type={"text"} size="small">뒤로 가기</Button>
            {
              watering.wateringDate !== '' && watering.wateringDate !== undefined
                ? <Button onClick={onSubmit} type="primary" size="small" shape="round">제출</Button>
                : <Button type="text" size="small" disabled>제출</Button>
            }
          </Space>
        </div>
      </Card>
    </div>
  )
}

export default WateringForm;
