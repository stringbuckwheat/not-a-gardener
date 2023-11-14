import React, {useState} from "react";
import wateringCodeDesign from "../../../../utils/dataArray/wateringCodeDesign";
import TodoCardFront from "./TodoCardFront";
import TodoCardBehind from "./TodoCardBehind";
import {Card} from "antd";
import Style from "./TodoCard.moduls.scss"

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
  let color = "danger";
  const wateringCode = garden.gardenDetail.wateringCode;

  if (wateringCode >= 0) {
    color = wateringCodeDesign[wateringCode].color;
  }

  const [clickedPlant, setClickedPlant] = useState(0);

  const flipCard = (plantId) => {
    setClickedPlant(() => plantId);
  }

  const props = {garden, color, flipCard};

  return (
    <Card className={Style.card} style={{borderRadius: "20px", borderWidth: "0.7px", border: "solid lightgray"}}>
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
