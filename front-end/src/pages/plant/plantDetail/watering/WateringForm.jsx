import locale from 'antd/es/date-picker/locale/ko_KR'; // '오늘' 같은 거
import 'dayjs/locale/ko'; // 달력 연월일요일 한국어로
import {Button, Card, Col, Row, Select, Space} from "antd";
import {DatePicker} from 'antd';
import {useState} from 'react';
import postData from 'src/api/backend-api/common/postData';
import getDisabledDate from 'src/utils/function/getDisabledDate';
import {useDispatch} from "react-redux";

// TODO WateringFormInCalendar와 같음
/**
 * 물주기 폼
 * @param plantId
 * @param closeForm
 * @param chemicalList
 * @param wateringCallBack
 * @returns {JSX.Element}
 * @constructor
 */
const WateringForm = ({plantId, closeForm, chemicalList, wateringCallBack, page}) => {
  const [watering, setWatering] = useState({plantId, chemicalId: chemicalList[0].value});
  const dispatch = useDispatch();

  const onSubmit = async () => {
    console.log("submit data", watering);

    try {
      const res = await postData(`/plant/${plantId}/watering?page=${page - 1}`, watering);
      console.log("res", res);
      dispatch({type: 'addWatering', payload: null})

      wateringCallBack(res);
    } catch (e) {
      if (e.code == "B005") {
        alert(e.message);
      }
    } finally {
      closeForm();
    }
  }

  return (
    <Row style={{justifyContent: "center"}}>
      <Col md={18}>
        <Card
          style={{borderColor: "green"}}
          onClose={closeForm}>
          <h5>물 주기</h5>
          <Row>
            <Col md={12} xs={24}>
              <small>언제 주었나요?</small>
              <DatePicker
                name="wateringDate"
                style={{width: "90%",}}
                disabledDate={getDisabledDate}
                onChange={(date, dateString) => setWatering(() => ({...watering, wateringDate: dateString}))}
                locale={locale}/>
            </Col>
            <Col md={12} xs={24}>
              <small>무엇을 주었나요?</small>
              <Select
                className="width-full"
                defaultValue="맹물"
                style={{width: "90%",}}
                onChange={(value) => setWatering(() => ({...watering, chemicalId: value}))}
                options={chemicalList}
                name="chemicalNo"
              />
            </Col>
          </Row>

          <div style={{marginTop: "1rem", display: "flex", justifyContent: "flex-end"}}>
            <Space>
              <Button onClick={closeForm} type={"text"} size="small">뒤로 가기</Button>
              {
                watering.wateringDate !== '' && watering.wateringDate !== undefined
                  ? <Button onClick={onSubmit} type="primary" size="sm" shape="rounded-pill">제출</Button>
                  : <Button type="text" size="sm" color='dark' variant="outline" disabled>제출</Button>
              }
            </Space>
          </div>
        </Card>
      </Col>
    </Row>
  )
}

export default WateringForm;
