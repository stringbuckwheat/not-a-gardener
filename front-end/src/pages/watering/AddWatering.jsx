
import locale from 'antd/es/date-picker/locale/ko_KR'; // '오늘' 같은 거
import 'dayjs/locale/ko'; // 달력 연월일요일 한국어로
import CIcon from "@coreui/icons-react";
import { cilDrop } from "@coreui/icons";
import { Select } from "antd";
import { CCol, CAlert } from "@coreui/react";
import { CButton, CRow } from "@coreui/react";
import { DatePicker, message } from 'antd';
import { useState } from 'react';
import authAxios from 'src/utils/interceptors';


const AddWatering = (props) => {
    const plantNo = props.plantNo;
    const closeForm = props.closeForm;
    const fertilizerList = props.fertilizerList;
    const openNotification = props.openNotification;
    const setWateringList = props.setWateringList;

    const [wateringDate, setWateringDate] = useState("");
    const [fertlizerNo, setFertilizerNo] = useState(0);

    const onSubmit = () => {
        authAxios.post("/watering", {
            plantNo: plantNo,
            fertilizerNo: fertlizerNo,
            wateringDate: wateringDate
        })
            .then((res) => {
                openNotification(res.data.wateringMsg);
                closeForm();
            })
            .then(() => {
                // watering list 바꾸기
                authAxios.get(`/plant/${plantNo}/watering`)
                    .then((res) => {
                        setWateringList(res.data);
                    })
            })
    }

    // 미래에 물준다고 기록하기 방지
    const disabledDate = (current) => {
        return current && current.valueOf() > Date.now();
    }

    return (
        <>
            <CAlert
                color="info"
                dismissible
                onClose={closeForm}>


                <CRow className="align-items-center">
                    <CCol md={1}>
                        <CIcon icon={cilDrop} size='lg' />
                    </CCol>
                    <CCol md={4}>
                        <DatePicker
                            name="wateringDate"
                            style={{ width: '100%' }}
                            disabledDate={disabledDate}
                            onChange={(date, dateString) => { setWateringDate(dateString) }}
                            locale={locale} />
                    </CCol>
                    <CCol md={5}>
                        <Select
                            defaultValue={0}
                            style={{ width: "100%" }}
                            onChange={(value) => { setFertilizerNo(value) }}
                            options={fertilizerList}
                            name="fertilizerNo"
                        />
                    </CCol>
                    <CCol md={2}>
                        {wateringDate !== ''
                            ? <CButton onClick={onSubmit} color="primary" size="sm" shape="rounded-pill">제출</CButton>
                            : <CButton type="button" size="sm" color='dark' variant="outline" disabled>제출</CButton>
                        }

                    </CCol>
                </CRow>
            </CAlert>
        </>
    )
}

export default AddWatering;
