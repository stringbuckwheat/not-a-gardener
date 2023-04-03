import locale from 'antd/es/date-picker/locale/ko_KR'; // '오늘' 같은 거
import 'dayjs/locale/ko'; // 달력 연월일요일 한국어로
import {Select, Space} from "antd";
import {CAlert} from "@coreui/react";
import {CButton} from "@coreui/react";
import {DatePicker} from 'antd';
import {useState} from 'react';
import postData from 'src/api/backend-api/common/postData';
import getDisabledDate from 'src/utils/function/getDisabledDate';


const WateringForm = (props) => {
  const {plantNo, closeForm, chemicalList, wateringCallBack} = props;
  const [wateringDate, setWateringDate] = useState("");
  const [chemicalNo, setChemicalNo] = useState(0);

  const onSubmit = async () => {
    const data = {
      plantNo: plantNo,
      chemicalNo: chemicalNo,
      wateringDate: wateringDate
    }

    const res = await postData("/watering", data);
    wateringCallBack(res);

    closeForm();
  }

  return (
    <>
      <CAlert
        color="info"
        onClose={closeForm}>
        <h5>물 주기</h5>
        <div className="mt-3 mb-1">
          <small>언제 주었나요</small>
          <DatePicker
            name="wateringDate"
            className="width-full"
            disabledDate={getDisabledDate}
            onChange={(date, dateString) => {
              setWateringDate(dateString)
            }}
            locale={locale}/>
        </div>
        <div className="mt-1 mb-1">
          <small>무엇을 주었나요?</small>
          <Select
            className="width-full"
            defaultValue="맹물"
            onChange={(value) => {
              setChemicalNo(value)
            }}
            options={chemicalList}
            name="chemicalNo"
          />
        </div>

        <div className="mt-3 d-flex justify-content-end">
          <Space>
            <CButton onClick={closeForm} type="button" size="sm" color='dark' variant="outline">뒤로 가기</CButton>
            {wateringDate !== ''
              ? <CButton onClick={onSubmit} color="primary" size="sm" shape="rounded-pill">제출</CButton>
              : <CButton type="button" size="sm" color='dark' variant="outline" disabled>제출</CButton>
            }
          </Space>
        </div>
      </CAlert>
    </>
  )
}

export default WateringForm;
