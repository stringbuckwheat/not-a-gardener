import React, { useEffect, useState } from "react";
import { CContainer, CRow, CCol, CWidgetStatsF, CLink } from "@coreui/react";
import { cilPlus } from "@coreui/icons";
import CIcon from '@coreui/icons-react';
import { useNavigate } from "react-router-dom";
import FertilizerCard from "../../components/card/FertilizerCard";
import authAxios from "src/utils/requestInterceptor";

const Fertilizer = () => {
    console.log("Fertilizer");

    const [fertilizerList, setFertilizerList] = useState([{
        fertilizerNo: 0,
        fertilizerName: "",
        fertilizerType: "",
        fertilizingPeriod: 0
    }]);

    const navigate = useNavigate();
    
    useEffect(() => {
        authAxios.get("/fertilizer")
        .then((res) => {
            console.log("res", res);
            setFertilizerList(res.data);
        })
    }, [])

    return (
        <CContainer fluid>
            <CRow>
                <CCol md={3} xs={12}>
                    <CWidgetStatsF
                        className="mb-3"
                        color="dark"
                        icon={<CIcon icon={cilPlus} height={30} />}
                        value="새로운 비료 추가"
                        onClick={() => {navigate("/fertilizer/add", {state: {
                                                                        fertilizerName: "",
                                                                        fertilizerType: "기본 NPK 비료",
                                                                        fertilizingPeriod: 14
                                                                    }})}}
                    />
                </CCol>
                
                {/* 카드 컴포넌트 반복 */}
                {fertilizerList.map((fertilizer) => (
                    <FertilizerCard fertilizer={fertilizer}/>
                ))}
            </CRow>
        </CContainer>
    );
}

export default Fertilizer;