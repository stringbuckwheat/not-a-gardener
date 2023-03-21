import { CCol, CWidgetStatsF } from "@coreui/react";
import { cilDrop, cilSmile, cilFaceDead, cilHandPointUp, cilSwimming, cilZoom } from "@coreui/icons";
import { Link } from "react-router-dom";
import CIcon from '@coreui/icons-react';
import GardenTag from "src/pages/garden/GardenTag";
import WateringDropdown from "../dropdown/WateringDropdown";
import { ArrowRightOutlined, RightOutlined } from "@ant-design/icons";
import { Space } from "antd";

const GardenCard = (props) => {
    const garden = props.plant;
    const plant = props.plant.plant;
    const gardenDetail = props.plant.gardenDetail;

    const fertilizerList = props.fertilizerList;
    const setPlantList = props.setPlantList;
    const openNotification = props.openNotification;

    // 음수           0                     1          2         3              4
    // 물주기 놓침   물주기 정보 부족    물주기     체크하기     놔두세요    오늘 물 줌

    let icon = {};
    let color = "primary";
    let wateringMsg = "";

    if (gardenDetail.wateringCode < 0) {
        // 물주기 놓침
        color = "danger" // 빨강
        wateringMsg = `물 주기를 ${gardenDetail.wateringDDay * -1}일 놓쳤어요!`
        icon = cilFaceDead;
    } else if (gardenDetail.wateringCode == 0) {
        // 물주기 정보 부족
        color = "primary"
        wateringMsg = "물주기 정보가 부족해요!"
        icon = cilZoom;
    } else if (gardenDetail.wateringCode == 1) {
        color = "primary" // 파랑
        wateringMsg = "오늘은 물 주는 날이에요!"
        icon = cilDrop;
    } else if (gardenDetail.wateringCode == 2) {
        color = "warning" // 노랑
        wateringMsg = "물 주기가 하루 남았어요. \n화분이 말랐는지 체크해보세요!"
        icon = cilHandPointUp;
    } else if (gardenDetail.wateringCode == 3) {
        color = "success" // 초록
        wateringMsg = "이 식물은 지금 행복해요. 가만히 두세요."
        icon = cilSmile;
    } else if (gardenDetail.wateringCode == 4) {
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
                            <GardenTag plant={garden} />
                            <div className={`mt-2 text-${color} new-line`}>
                                {wateringMsg}
                            </div>
                        </CCol>
                    </>}
                value={
                    <Link
                        className="no-text-decoration text-black"
                        to={`/plant/${plant.plantNo}`}>
                            <Space>
                        {plant.plantName}
                        <ArrowRightOutlined
                            className="vertical-align-middle"/>
                        </Space>
                    </Link>
                }
                footer={
                    <WateringDropdown
                        plantNo={plant.plantNo}
                        fertilizerList={fertilizerList}
                        setPlantList={setPlantList}
                        openNotification={openNotification} />
                }
            />
        </CCol>
    )
}

export default GardenCard;