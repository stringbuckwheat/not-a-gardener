import { CCol, CWidgetStatsF } from "@coreui/react";
import { cilBug, cilMoodVeryBad, cilCat, cilDrop } from "@coreui/icons";
import CIcon from '@coreui/icons-react';
import { useNavigate } from "react-router-dom";


const PesticideCard = (props) => {
    const navigate = useNavigate();
    const pesticide = props.pesticide;

    let color = "";
    let icon = {};

    if(pesticide.pesticideType === "살충제"){
        color = "primary";
        icon = cilBug;
    } else if(pesticide.pesticideType === "살균제"){
        color = "warning";
        icon = cilMoodVeryBad;
    } else if(pesticide.pesticideType === "천적 방제"){
        color = "success";
        icon = cilCat;
    } else {
        color = "info";
        icon = cilDrop;
    }

    return(
        <CCol md={3} xs={12}>
            <CWidgetStatsF
                className="mb-3"
                onClick={() => navigate("/pesticide/" + pesticide.pesticideNo, {state: pesticide})}
                color={color}
                icon={<CIcon icon={icon} height={30} />}
                title={
                    <>
                        <div>{pesticide.pesticideType}</div>
                        <div className="mt-2"><small>{pesticide.pesticidePeriod}일에 한 번</small></div>
                    </>}
                value={
                    <>
                        <div className="d-flex justify-content-between">
                            <div>{pesticide.pesticideName}</div>
                        </div>
                    </>
                    }
                />
        </CCol>
    )
}

export default PesticideCard;