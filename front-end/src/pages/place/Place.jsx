import React, { useEffect, useState } from "react";
import { CContainer, CRow, CCol, CWidgetStatsF } from "@coreui/react";
import { cilPlus } from "@coreui/icons";
import CIcon from '@coreui/icons-react';
import { useNavigate } from "react-router-dom";
import PlaceCard from "../../components/card/PlaceCard";
import authAxios from "src/utils/interceptors";

const Place = () => {
    const [placeList, setPlaceList] = useState([{
        placeNo: 0,
        placeName: "",
        option: '',
        artificialLight: '',
        plantQuantity: -1,
        plantListSize: 0
    }]);

    const navigate = useNavigate();

    useEffect(() => {
        authAxios.get("/place")
            .then((res) => {
                console.log("Place res.data", res.data);
                setPlaceList(res.data);
            })
            .catch((error) => {
                console.log("error");
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
                        onClick={() => { navigate("/place/add") }}
                    />
                </CCol>

                {/* 카드 컴포넌트 반복 */}
                {placeList.map((place) => (
                    <PlaceCard place={place} />
                ))}
            </CRow>
        </CContainer>
    );
}

export default Place;