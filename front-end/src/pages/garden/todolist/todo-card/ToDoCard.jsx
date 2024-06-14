import React, { useState } from "react";
import TodoCardFront from "./TodoCardFront";
import TodoCardBehind from "./TodoCardBehind";
import { Card } from "antd";
import Style from "./TodoCard.module.scss";
import WateringCode from "../../../../utils/code/wateringCode";

function getWateringColor(wateringCode) {
  switch (wateringCode) {
    case WateringCode.LATE_WATERING:
      return "#dc3545";
    case WateringCode.NOT_ENOUGH_RECORD:
    case WateringCode.THIRSTY:
      return "#007BFF";
    case WateringCode.CHECK:
      return "orange";
    case WateringCode.LEAVE_HER_ALONE:
    case WateringCode.WATERED_TODAY:
      return "green";
    case WateringCode.YOU_ARE_LAZY:
      return "grey";
    default:
      return "black";
  }
}

const ToDoCardInfo = ({ index, garden, openNotification }) => {
  const wateringCode = garden.gardenDetail.wateringCode;
  console.log(garden.plant.name, " - ", wateringCode, " - ", getWateringColor(wateringCode));

  const [clickedPlant, setClickedPlant] = useState(0);

  const flipCard = (plantId) => {
    setClickedPlant(() => plantId);
  };

  const props = { garden, flipCard, color: getWateringColor(wateringCode) };

  return (
    <Card bodyStyle={{ width: "100%" }} className={Style.card}>
      {clickedPlant !== 0 ? (
        <TodoCardBehind {...props} openNotification={openNotification} index={index} />
      ) : (
        <TodoCardFront {...props} />
      )}
    </Card>
  );
};

export default ToDoCardInfo;
