import React from "react";
import { CContainer, CRow, CCol, CWidgetStatsF, CLink } from "@coreui/react";
import CIcon from '@coreui/icons-react';
import { cilLightbulb, cilHouse, cilEco, cilWindow, cilPlus } from "@coreui/icons";
import { useNavigate } from "react-router-dom";

const Place = () => {
    console.log("Place");

    const test = [{
        placeNo: 1,
        placeName: '창가',
        option: '실내',
        artificialLight: 'N',
        plantQuantity: 5
    },
    {
        placeNo: 2,
        placeName: '책상 위',
        option: '실내',
        artificialLight: 'Y',
        plantQuantity: 8
    },
    {
        placeNo: 3,
        placeName: '선반 위',
        option: '실내',
        artificialLight: 'N',
        plantQuantity: 3
    },
    {
        placeNo: 4,
        placeName: '정원4',
        option: '베란다',
        artificialLight: 'N',
        plantQuantity: 0
    },
    {
        placeNo: 4,
        placeName: '정원5',
        option: '야외',
        artificialLight: 'Y',
        plantQuantity: 6
    }]

    // 실내, 베란다, 야외
    const options = ["실내", "베란다", "야외"];
    const color = ["primary", "warning", "success"];

    const navigate = useNavigate();

    const placeDetail = (placeNo) => {
        console.log("placeNo " + placeNo + "번의 상세정보");
    }

    return (
        <>
        <CContainer fluid className="d-flex">
            <CRow>
                <CCol md={3} xs={12}>
                    <CWidgetStatsF
                        className="mb-3"
                        color="dark"
                        icon={<CIcon icon={cilPlus} height={30} />}
                        value="새로운 장소 추가"
                        onClick={() => {navigate("/place/add")}}
                        />
                </CCol>
                {test.map((place, idx) => {
                    let color = "";
                    let icon = {};

                    if(place.option === "실내"){
                        color = "success";
                        icon = cilHouse;
                    } else if(place.option === "베란다") {
                        color = "primary";
                        icon = cilWindow;
                    } else if(place.option === "야외"){
                        color = "warning";
                        icon = cilEco;
                    }

                    return(
                        
                        <CCol md={3} xs={12}>
                            <CWidgetStatsF
                                className="mb-3"
                                onClick={() => navigate("/place/" + place.placeNo, {state: place})}
                                color={color}
                                icon={<CIcon icon={icon} height={30} />}
                                title={
                                    <>
                                        <div>{place.option}</div>
                                        <div className="mt-2"><small>{place.plantQuantity}개의 식물이 살고 있어요</small></div>
                                    </>}
                                value={
                                    <>
                                        <div className="d-flex justify-content-between">
                                            <div>{place.placeName}</div>
                                            {place.artificialLight === 'Y'
                                            ? <div> <CIcon icon={cilLightbulb}/></div>
                                            : <></>}
                                        </div>
                                    </>
                                    }
                                />
                        </CCol>
                    )
                })}
            </CRow>
        </CContainer>
        </>
    );
}

export default Place;