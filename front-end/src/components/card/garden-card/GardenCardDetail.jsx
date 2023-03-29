import CIcon from "@coreui/icons-react";
import GardenTag from "../../../pages/garden/GardenTag";
import {Link} from "react-router-dom";
import {CButton, CWidgetStatsF} from "@coreui/react";
import {cilDrop, cilFaceDead, cilHandPointUp, cilSmile, cilSwimming, cilZoom} from "@coreui/icons";
import {useState} from "react";
import {Button} from "antd";
import GardenCardAnimatedButton from "../../../pages/garden/GardenCardAnimatedButton";

const GardenCardDetail = (props) => {
  const garden = props.garden;
  const gardenDetail = props.garden.gardenDetail;
  const plant = props.garden.plant;

  // 음수           0                     1          2         3              4
  // 물주기 놓침   물주기 정보 부족    물주기     체크하기     놔두세요    오늘 물 줌

  let icon = {};
  let color = "primary";
  let wateringMsg = "";

  if (gardenDetail.wateringCode < 0) {
    // 물주기 놓침
    color = "danger" // 빨강
    wateringMsg = `물 주기를 ${gardenDetail.wateringDDay * -1}일 놓쳤어요!\n비료를 주지 마세요`
    icon = cilFaceDead;
  } else if (gardenDetail.wateringCode == 0) {
    // 물주기 정보 부족
    color = "primary"
    wateringMsg = "물주기 정보가 부족해요!"
    icon = cilZoom;
  } else if (gardenDetail.wateringCode == 1) {
    color = "primary" // 파랑

    if (gardenDetail.fertilizingCode != -1) {
      wateringMsg = `오늘은 물 주는 날이에요. \n${gardenDetail.fertilizingCode}번 비료를 주세요!`
    } else {
      wateringMsg = `오늘은 물 주는 날이에요. \n맹물을 주세요!`
    }

    icon = cilDrop;
  } else if (gardenDetail.wateringCode == 2) {
    color = "warning" // 노랑
    wateringMsg = "물 주기가 하루 남았어요. \n화분이 말랐는지 체크해보세요!\n말랐다면"

    if (gardenDetail.fertilizingCode != -1) {
      wateringMsg += ` ${gardenDetail.fertilizingCode}번 비료를 주세요!`
    } else {
      wateringMsg += ` 맹물을 주세요!`
    }

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

  const [hovered, setHovered] = useState(false);
  const [isWaterFormOpen, setIsWaterFormOpen] = useState(false);
  const [isPostponeFormOpen, setIsPostponeFormOpen] = useState(false);

  return (
    <div
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}>

      {
        hovered
          ? <GardenCardAnimatedButton
            setIsWaterFormOpen={setIsWaterFormOpen}
            setIsPostponeFormOpen={setIsPostponeFormOpen}/>
          : <></>
      }

      {
        !isWaterFormOpen
          ?
          <Link
            className="no-text-decoration"
            to={`/plant/${plant.plantNo}`}>
            <CWidgetStatsF
              style={{minHeight: "22vh"}}
              className="mb-2 width-full center"
              color={color}
              icon={<CIcon icon={icon} height={40}/>}
              title={
                <>
                  <small>{`${plant.plantSpecies} / ${plant.placeName}`}</small>
                  <GardenTag plant={garden}/>
                  <div className={`mt-2 text-${color} new-line`}>
                    {wateringMsg}
                  </div>
                </>
              }
              value={
                <span className="text-black">{plant.plantName}</span>
              }/>
          </Link>

          :
          <div
            style={{minHeight: "22vh", backgroundColor: "white", borderRadius: "5%"}}
            className="mb-2 width-full center">
            <CWidgetStatsF
              style={{minHeight: "22vh"}}
              className="mb-2 width-full center"

              // color={color}
              // icon={<CIcon icon={icon} height={40}/>}
              title={
                <>
                  <div className={`text-${color} new-line mb-2`}>
                    {wateringMsg}
                  </div>
                  <Button className={"mb-1"} size={"small"}>시키는대로 준 경우</Button>
                  <Button size={"small"}>커스텀이 필요한 경우</Button>
                </>
              }
              value={
                <span>{plant.plantName}</span>
              }/>
          </div>
      }
    </div>
  )
}

export default GardenCardDetail;
