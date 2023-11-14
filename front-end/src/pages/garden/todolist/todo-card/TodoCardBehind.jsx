import React, {useState} from "react";
import TodoCardButton from "./TodoCardButton";
import TodoCardWatering from "./TodoCardWatering";
import getWateringMsg from "../../../../utils/function/getWateringMsg";
import {Row} from "antd";
import {LeftOutlined} from "@ant-design/icons";

const TodoCardBehind = ({openNotification, index, flipCard, garden, color}) => {
  // 무슨 버튼을 눌렀는지
  const [selected, setSelected] = useState("");

  return (
    <div>
      <div onClick={() => flipCard(0)} style={{margin: "0.7rem"}}>
        <div className={"d-flex justify-content-between"} >
          <span className={`fs-6 fw-semibold text-black`}>{garden.plant.name}</span>
          <LeftOutlined/>
        </div>
        <p
          className={`text-${color} fw-semibold small new-line mt-1`}>
          {getWateringMsg(garden.gardenDetail)}
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
