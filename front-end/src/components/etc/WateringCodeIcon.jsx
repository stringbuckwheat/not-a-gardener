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

const wateringCodeDesign = [
  // 0    관측 중
  <MonitorOutlined style={{fontSize: "2rem", color: "#007BFF"}}/>,
  // 1    물주기
  <CoffeeOutlined style={{fontSize: "2rem", color: "#007BFF"}}/>,
  // 2    체크하기
  <BellOutlined style={{fontSize: "2rem", color: "orange"}}/>,
  // 3    물주기 늘어나는 중
  <RiseOutlined style={{fontSize: "2rem", color: "green"}}/>,
  // 4    놔두세요
  <SmileOutlined style={{fontSize: "2rem", color: "green"}}/>,
  // 5    오늘 물 줌
  <LikeOutlined style={{fontSize: "2rem", color: "green"}}/>,
  // 6    오늘 미룸
  <MehOutlined style={{fontSize: "2rem", color: "grey"}}/>
]

const wateringCodeDesignLg = [
  // 0    관측 중
  <MonitorOutlined style={{fontSize: "3.5rem", color: "#007BFF"}}/>,
  // 1    물주기
  <CoffeeOutlined style={{fontSize: "3.5rem", color: "#007BFF"}}/>,
  // 2    체크하기
  <BellOutlined style={{fontSize: "3.5rem", color: "orange"}}/>,
  // 3    물주기 늘어나는 중
  <RiseOutlined style={{fontSize: "3.5rem", color: "green"}}/>,
  // 4    놔두세요
  <SmileOutlined style={{fontSize: "3.5rem", color: "green"}}/>,
  // 5    오늘 물 줌
  <LikeOutlined style={{fontSize: "3.5rem", color: "green"}}/>,
  // 6    오늘 미룸
  <MehOutlined style={{fontSize: "3.5rem", color: "grey"}}/>
]

const WateringCodeIcon = ({wateringCode, size = "2rem", wateringMsg}) => {
  let icon = <FrownOutlined style={{fontSize: size, color: "#dc3545"}}/>;

  if (size == "2rem" && wateringCode >= 0) {
    icon = wateringCodeDesign[wateringCode];
  } else if (size == "3.5rem" && wateringCode >= 0) {
    icon = wateringCodeDesignLg[wateringCode];
  }

  if (wateringMsg) {
    return (
      <Tooltip placement="topLeft" title={wateringMsg}
               className="new-line" overlayStyle={{whiteSpace: 'pre-line'}}>
        {icon}
      </Tooltip>
    )
  }

  return (
    <>
      {icon}
    </>
  )
}

export default WateringCodeIcon;
