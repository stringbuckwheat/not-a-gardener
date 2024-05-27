import React from "react";
import {Tooltip} from "antd";
import {FrownOutlined} from "@ant-design/icons";
import {
  BellOutlined,
  CoffeeOutlined,
  LikeOutlined, MehOutlined,
  MonitorOutlined,
  RiseOutlined,
  SmileOutlined
} from "@ant-design/icons";
import WateringCode from "../../utils/code/wateringCode";

const getWateringIcon = (wateringCode, size) => {
  switch (wateringCode) {
    case WateringCode.LATE_WATERING:
      return <FrownOutlined style={{fontSize: size, color: "#dc3545"}}/>;
    case WateringCode.NOT_ENOUGH_RECORD:
      return <MonitorOutlined style={{fontSize: size, color: "#007BFF"}}/>;
    case WateringCode.THIRSTY:
      return <CoffeeOutlined style={{fontSize: size, color: "#007BFF"}}/>;
    case WateringCode.CHECK:
      return <BellOutlined style={{fontSize: size, color: "orange"}}/>;
    case WateringCode.LEAVE_HER_ALONE:
      return <SmileOutlined style={{fontSize: size, color: "green"}}/>;
    case WateringCode.WATERED_TODAY:
      return <LikeOutlined style={{fontSize: size, color: "green"}}/>;
    case WateringCode.YOU_ARE_LAZY:
      return <MehOutlined style={{fontSize: size, color: "grey"}}/>;
    default:
      return <RiseOutlined style={{fontSize: size, color: "black"}}/>;
  }
}

const WateringCodeIcon = ({wateringCode, size = "2rem", wateringMsg}) => {
  if (wateringMsg) {
    return (
      <Tooltip placement="topLeft" title={wateringMsg}
               className="new-line" overlayStyle={{whiteSpace: 'pre-line'}}>
        {getWateringIcon(wateringCode, size)}
      </Tooltip>
    )
  }

  return (
    <>
      {getWateringIcon(wateringCode, size)}
    </>
  )
}

export default WateringCodeIcon;
