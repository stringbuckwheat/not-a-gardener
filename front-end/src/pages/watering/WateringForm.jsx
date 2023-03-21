
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
import getChemicalListForSelect from 'src/api/service/getChemicalListForSelect';


const WateringForm = (props) => {
    const plantNo = props.plantNo;
    const closeForm = props.closeForm;
    const openNotification = props.openNotification;
    const setWateringList = props.setWateringList;

    const [chemicalList, setChemicalList] = useState([]);

    const [wateringDate, setWateringDate] = useState("");
    const [chemicalNo, setChemicalNo] = useState(0);


    useEffect(() => {
        getChemicalListForSelect(setChemicalList);
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
                        className="width-full"
                        disabledDate={disabledDate}
                        onChange={(date, dateString) => { setWateringDate(dateString) }}
                        locale={locale} />
                </div>
                <div className="mt-1 mb-1">
                    <small>무엇을 주었나요?</small>
                    <Select
                        className="width-full"
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

export default WateringForm;
