import { CButton, CCol, CLink, CRow, CWidgetStatsF } from "@coreui/react";
import { cilDrop, cilSmile, cilFaceDead, cilHandPointUp, cilSwimming, cilZoom, cilArrowRight } from "@coreui/icons";
import { Link } from "react-router-dom";
import CIcon from '@coreui/icons-react';
import { useNavigate } from "react-router-dom";
import GardenTag from "src/pages/garden/GardenTag";
import WateringDropdown from "../dropdown/WateringDropdown";

const GardenCard = (props) => {
    const plant = props.plant;
    const fertilizerList = props.fertilizerList;
    const setPlantList = props.setPlantList;
    const openNotification= props.openNotification;

    const navigate = useNavigate();

    const plantUrl = `/plant/${plant.plantNo}`

    // 음수           0                     1          2         3              4
    // 물주기 놓침   물주기 정보 부족    물주기     체크하기     놔두세요    오늘 물 줌

    let icon = {};
    let color = "primary";
    let wateringMsg = "";

    if (plant.wateringCode < 0) {
        // 물주기 놓침
        color = "danger" // 빨강
        wateringMsg = `물 주기를 ${plant.wateringDDay * -1}일 놓쳤어요!`
        icon = cilFaceDead;
    } else if (plant.wateringCode == 0) {
        // 물주기 정보 부족
        color = "primary"
        wateringMsg = "물주기 정보가 부족해요!"
        icon = cilZoom;
    } else if (plant.wateringCode == 1) {
        color = "primary" // 파랑
        wateringMsg = "오늘은 물 주는 날이에요!"
        icon = cilDrop;
    } else if (plant.wateringCode == 2) {
        color = "warning" // 노랑
        wateringMsg = "물 주기가 하루 남았어요. 화분이 말랐는지 체크해보세요!"
        icon = cilHandPointUp;
    } else if (plant.wateringCode == 3) {
        color = "success" // 초록
        wateringMsg = "이 식물은 지금 행복해요. 가만히 두세요."
        icon = cilSmile;
    } else if (plant.wateringCode == 4) {
        color = "success" // 초록
        wateringMsg = "이 식물은 오늘 물을 마셨어요!"
        icon = cilSwimming;
    }
    
    return (
        <CCol md={4} xs={12}>
            <CWidgetStatsF
                className="mb-3"
                color={color}
                icon={<CIcon icon={icon} height={40} />}
                title={
                    <>
                        <CCol>
                            <div>
                                <small>{plant.plantSpecies}</small>
                            </div>
                            <GardenTag plant={plant} />
                            <div className="mt-1">
                                <small>{wateringMsg}</small>
                            </div>
                        </CCol>
                    </>}
                value={
                    <Link to={plantUrl} style={{ textDecoration: "none", color: "black" }}>
                        {plant.plantName}
                    </Link>
                }
                footer={
                        <WateringDropdown 
                            plantNo={plant.plantNo} 
                            fertilizerList={fertilizerList}
                            setPlantList={setPlantList}
                            openNotification={openNotification}/>
                }
            />
        </CCol>
    )
}

export default GardenCard;