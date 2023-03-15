import React, { useEffect, useState } from "react";
import { CRow, CCol, CWidgetStatsF } from "@coreui/react";
import { cilPlus } from "@coreui/icons";
import CIcon from '@coreui/icons-react';
import PlaceCard from "../../components/card/PlaceCard";
import onMount from "src/api/service/onMount";
import { Link } from "react-router-dom";
import ListHeader from "src/data/header/ListHeader";
import NoItem from "src/components/NoItem";
import AddItemButton from "src/components/button/AddItemButton";

const Place = () => {
    const [placeList, setPlaceList] = useState([{
        placeNo: 0,
        placeName: "",
        option: '',
        artificialLight: '',
        plantQuantity: -1,
        plantListSize: 0,
        createDate: ''
    }]);

    // add시에는 페이지 이동때문에 자동으로 마운트됨
    useEffect(() => {
        onMount("/place", setPlaceList);
    }, [])

    return (
        <>
            {
                placeList.length == 0
                    ? <NoItem title="등록된 장소가 없어요" button={<AddItemButton addUrl="/place/add" size="lg" title="장소 추가하기"/>} />
                    :
                    <>
                        <ListHeader placeList={placeList} setPlaceList={setPlaceList} />
                        <CRow>
                            <CCol md={3} xs={12}>
                                <Link to="/place/add" style={{ textDecoration: "none" }}>
                                    <CWidgetStatsF
                                        className="mb-3"
                                        color="dark"
                                        icon={<CIcon icon={cilPlus} height={30} />}
                                        value="새로운 장소 추가"
                                    />
                                </Link>
                            </CCol>

                            {/* 카드 컴포넌트 반복 */}
                            {placeList.map((place) => (
                                <PlaceCard place={place} />
                            ))}
                        </CRow>
                    </>

            }
        </>
    );
}

export default Place;