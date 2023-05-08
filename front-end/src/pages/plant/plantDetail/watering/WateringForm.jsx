import locale from 'antd/es/date-picker/locale/ko_KR'; // '오늘' 같은 거
import 'dayjs/locale/ko'; // 달력 연월일요일 한국어로
import {Select, Space} from "antd";
import {CAlert, CCol, CRow} from "@coreui/react";
import {CButton} from "@coreui/react";
import {DatePicker} from 'antd';
import {useState} from 'react';
import postData from 'src/api/backend-api/common/postData';
import getDisabledDate from 'src/utils/function/getDisabledDate';

/**
 * 물주기 폼
 * @param plantId
 * @param closeForm
 * @param chemicalList
 * @param wateringCallBack
 * @returns {JSX.Element}
 * @constructor
 */
const WateringForm = ({plantId, closeForm, chemicalList, wateringCallBack}) => {
  const [watering, setWatering] = useState({plantId, chemicalId: chemicalList[0].value});

  const onSubmit = async () => {
    console.log("submit data", watering);
    let res;

    try {
      res = await postData(`/plant/${plantId}/watering`, watering);
      console.log("res", res);
    } catch (e) {
      alert(e.response.data.errorDescription);
    } finally {
      closeForm();
    }

    wateringCallBack(res);
  }

  return (
    <CRow className="d-flex justify-content-center mt-1">
      <CCol md={10}>
        <CAlert
          color="info"
          onClose={closeForm}>
          <h5>물 주기</h5>
          <CRow className="mt-1 mb-1">
            <CCol md={6} xs={12}>
              <small>언제 주었나요</small>
              <DatePicker
                name="wateringDate"
                className="width-full"
                disabledDate={getDisabledDate}
                onChange={(date, dateString) => setWatering(() => ({...watering, wateringDate: dateString}))}
                locale={locale}/>
            </CCol>
            <CCol md={6} xs={12}>
              <small>무엇을 주었나요?</small>
              <Select
                className="width-full"
                defaultValue="맹물"
                onChange={(value) => setWatering(() => ({...watering, chemicalId: value}))}
                options={chemicalList}
                name="chemicalNo"
              />
            </CCol>
          </CRow>

          <div className="mt-3 d-flex justify-content-end">
            <Space>
              <CButton onClick={closeForm} type="button" size="sm" color='dark' variant="outline">뒤로 가기</CButton>
              {watering.wateringDate !== ''
                ? <CButton onClick={onSubmit} color="primary" size="sm" shape="rounded-pill">제출</CButton>
                : <CButton type="button" size="sm" color='dark' variant="outline" disabled>제출</CButton>
              }
            </Space>
          </div>
        </CAlert>
      </CCol>
    </CRow>
  )
}

export default WateringForm;
