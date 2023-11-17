import React, {useState} from "react";
import TodoCardFront from "./TodoCardFront";
import TodoCardBehind from "./TodoCardBehind";
import {Card} from "antd";
import Style from "./TodoCard.module.scss"

/**
 * 메인페이지 할 일 카드
 * @param index
 * @param deleteInTodoList
 * @param garden
 * @param openNotification
 * @returns {JSX.Element}
 * @constructor
 */
const ToDoCardInfo = ({
                        index,
                        garden,
                        openNotification,
                      }) => {
  let color = "#dc3545";
  const colors = ["#007BFF", "#007BFF", "orange", "green", "green", "green", "grey"];
  const wateringCode = garden.gardenDetail.wateringCode;

  if (wateringCode >= 0) {
    color = colors[wateringCode];
  }

  const [clickedPlant, setClickedPlant] = useState(0);

  const flipCard = (plantId) => {
    setClickedPlant(() => plantId);
  }

  const props = {garden, flipCard, color};

  return (
    <Card
      bodyStyle={{width: "100%"}}
      className={Style.card}>
      {
        clickedPlant !== 0 ?
          <TodoCardBehind
            {...props}
            openNotification={openNotification}
            index={index}
          />
          : <TodoCardFront {...props} />
      }
    </Card>
  )
}

export default ToDoCardInfo;
