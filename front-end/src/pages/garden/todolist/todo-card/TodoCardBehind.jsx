import React, {useState} from "react";
import TodoCardButton from "./TodoCardButton";
import TodoCardWatering from "./TodoCardWatering";
import getWateringMsg from "../../../../utils/function/getWateringMsg";
import {LeftOutlined} from "@ant-design/icons";
import Style from "./TodoCard.module.scss"

const TodoCardBehind = ({openNotification, index, flipCard, garden, color}) => {
  // 무슨 버튼을 눌렀는지
  const [selected, setSelected] = useState("");

  return (
    <div>
      <div onClick={() => flipCard(0)} className={Style.behindInfo}>
        <div style={{display: "flex", justifyContent: "space-between"}}>
          <span style={{fontSize: "1rem", fontWeight: "600", color: "black"}}>{garden.plant.name}</span>
          <LeftOutlined/>
        </div>
        <p
          style={{color: color, fontWeight: "600", marginTop: "0.3rem"}}
          className={`new-line`}>
          <small>{getWateringMsg(garden.gardenDetail)}</small>
        </p>
      </div>
      {
        selected == "" ?
          <TodoCardButton
            plantId={garden.plant.id}
            setSelected={setSelected}
            openNotification={openNotification}
            index={index}
          />
          :
          selected == "watered" ?
            <TodoCardWatering
              plantId={garden.plant.id}
              setSelected={setSelected}
              openNotification={openNotification}
              flipCard={flipCard}
            />
            : <></>
      }
    </div>)

}

export default TodoCardBehind
