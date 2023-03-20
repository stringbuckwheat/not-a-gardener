
import locale from 'antd/es/date-picker/locale/ko_KR'; // '오늘' 같은 거
import 'dayjs/locale/ko'; // 달력 연월일요일 한국어로
import CIcon from "@coreui/icons-react";
import { cilDrop } from "@coreui/icons";
import { Select, Space } from "antd";
import { CAlert } from "@coreui/react";
import { CButton } from "@coreui/react";
import { DatePicker } from 'antd';
import { useEffect, useState } from 'react';
import getData from 'src/api/backend-api/common/getData';
import insertData from 'src/api/backend-api/common/insertData';


const AddWatering = (props) => {
    const plantNo = props.plantNo;
    const closeForm = props.closeForm;
    const openNotification = props.openNotification;
    const setWateringList = props.setWateringList;

    const [chemicalList, setChemicalList] = useState([]);

    const [wateringDate, setWateringDate] = useState("");
    const [chemicalNo, setChemicalNo] = useState(0);

    const onMountAddWatering = async () => {
        const data = await getData("/chemical");
        console.log("data", data);

        // 맨 앞에 맹물 추가
        data.unshift({
            chemicalNo: 0,
            chemicalName: '맹물'
        })

        // select 요구 사항에 맞게 배열 가공
        setChemicalList(data.map((chemical) => ({
            value: chemical.chemicalNo,
            label: chemical.chemicalName
        })))
    }

    useEffect(() => {
        onMountAddWatering();
    }, [])

    const onSubmit = async () => {
        const data = {
            plantNo: plantNo,
            chemicalNo: chemicalNo,
            wateringDate: wateringDate
        }

        const res = await insertData("/watering", data);
        closeForm();
        openNotification(res.wateringMsg);

        const wateringList = await getData(`/plant/${plantNo}/watering`);
        setWateringList(wateringList);
    }

    // 미래에 물준다고 기록하기 방지
    const disabledDate = (current) => {
        return current && current.valueOf() > Date.now();
    }

    return (
        <>
            <CAlert
                color="info"
                onClose={closeForm}>
                <h5><CIcon icon={cilDrop} size='lg' /> 물 주기</h5>
                <div className="mt-3 mb-1">
                    <small>언제 주었나요</small>
                    <DatePicker
                        name="wateringDate"
                        className="width-100"
                        disabledDate={disabledDate}
                        onChange={(date, dateString) => { setWateringDate(dateString) }}
                        locale={locale} />
                </div>
                <div className="mt-1 mb-1">
                    <small>무엇을 주었나요?</small>
                    <Select
                        className="width-100"
                        defaultValue="맹물"
                        onChange={(value) => { setChemicalNo(value) }}
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

export default AddWatering;
