import React, { useEffect, useState } from "react";
import { CContainer, CRow, CCol, CWidgetStatsF, CLink } from "@coreui/react";
import { cilPlus } from "@coreui/icons";
import CIcon from '@coreui/icons-react';
import { useNavigate } from "react-router-dom";
import PlaceCard from "./PlaceCard";
import authAxios from "src/utils/requestInterceptor";

const Place = () => {
    console.log("Place");

    const [placeList, setPlaceList] = useState([{
        placeNo: 0,
        placeName: "",
        option: '',
        artificialLight: '',
        plantQuantity: -1
    }]);

    const navigate = useNavigate();
    
    useEffect(() => {
        authAxios.get("/place")
        .then((res) => {
            console.log("res", res);
            setPlaceList(res.data);
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
                        value="새로운 장소 추가"
                        onClick={() => {navigate("/place/add", {state: {placeName: "나의 장소",
                                                                        option: "실내",
                                                                        artificialLight: false}})}}
                        />
                </CCol>
                
                {/* 카드 컴포넌트 반복 */}
                {placeList.map((place) => (
                    <PlaceCard place={place}/>
                ))}
            </CRow>
        </CContainer>
    );
}

export default Place;