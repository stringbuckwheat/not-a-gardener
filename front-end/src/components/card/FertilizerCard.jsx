import { CCol, CWidgetStatsF } from "@coreui/react";
import { cilRestaurant, cilFlower, cilMugTea, cilDrop } from "@coreui/icons";
import CIcon from '@coreui/icons-react';
import { useNavigate } from "react-router-dom";


const FertilizerCard = (props) => {
    const navigate = useNavigate();
    const fertilizer = props.fertilizer;

    let color = "";
    let icon = {};

    // 기본 NPK 비료
    // 개화용 비료
    // 미량 원소 비료
    // 그 외

    if(fertilizer.fertilizerType === "기본 NPK 비료"){
        color = "primary";
        icon = cilRestaurant;
    } else if(fertilizer.fertilizerType === "개화용 비료"){
        color = "warning";
        icon = cilFlower;
    } else if(fertilizer.fertilizerType === "미량 원소 비료"){
        color = "success";
        icon = cilMugTea;
    } else {
        color = "info";
        icon = cilDrop;
    }

    return(
        <CCol md={3} xs={12}>
            <CWidgetStatsF
                className="mb-3"
                onClick={() => navigate("/fertilizer/" + fertilizer.fertilizerNo, {state: fertilizer})}
                color={color}
                icon={<CIcon icon={icon} height={30} />}
                title={
                    <>
                        <div>{fertilizer.fertilizerType}</div>
                        <div className="mt-2"><small>{fertilizer.fertilizingPeriod}일에 한 번</small></div>
                    </>}
                value={
                    <>
                        <div className="d-flex justify-content-between">
                            <div>{fertilizer.fertilizerName}</div>
                        </div>
                    </>
                    }
                />
        </CCol>
    )
}

export default FertilizerCard;
