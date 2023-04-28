import CIcon from "@coreui/icons-react";
import React from "react";
import {cilFaceDead} from "@coreui/icons";
import wateringCodeDesign from "../../utils/dataArray/wateringCodeDesign";
import {Tooltip} from "antd";

const WateringCodeIcon = ({wateringCode, height = 45, wateringMsg}) => {
  let color = "danger";
  let icon = cilFaceDead;

  if (wateringCode >= 0) {
    color = wateringCodeDesign[wateringCode].color;
    icon = wateringCodeDesign[wateringCode].icon;
  }

  if (wateringMsg) {
    return (
      <Tooltip placement="topLeft" title={wateringMsg}
               className="new-line" overlayStyle={{whiteSpace: 'pre-line'}}>
        <CIcon className={`me-3 text-${color}`} icon={icon} height={height}/>
      </Tooltip>
    )
  }

  return (
    <CIcon className={`me-3 text-${color}`} icon={icon} height={height}/>
  )
}

export default WateringCodeIcon;
