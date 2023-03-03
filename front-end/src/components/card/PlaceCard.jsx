import { CCol, CWidgetStatsF } from "@coreui/react";
import { cilLightbulb, cilHouse, cilEco, cilWindow } from "@coreui/icons";
import CIcon from '@coreui/icons-react';
import { useNavigate } from "react-router-dom";


const PlaceCard = (props) => {
    const navigate = useNavigate();
    const place = props.place;

    console.log("place", place)

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
                        <div className="mt-2"><small>{place.plantList.length}개의 식물이 살고 있어요</small></div>
                    </>}
                value={
                    <>
                        <div className="d-flex justify-content-between">
                            <div>{place.placeName}</div>
                            {place.artificialLight === "사용"
                            ? <div> <CIcon icon={cilLightbulb}/></div>
                            : <></>}
                        </div>
                    </>
                    }
                />
        </CCol>
    )
}

export default PlaceCard;