import React, {useState} from "react";
import TodoCardButton from "./TodoCardButton";
import TodoCardWatering from "./TodoCardWatering";
import getWateringMsg from "../../../../utils/function/getWateringMsg";
import {LeftOutlined} from "@ant-design/icons";
import Style from "./TodoCard.module.scss"
import {Button, ConfigProvider} from "antd";
import themeGreen from "../../../../themeGreen";
import GButton from "../../../../components/button/GButton";
import {useNavigate} from "react-router-dom";

const TodoCardBehind = ({openNotification, index, flipCard, garden, color}) => {
  // 무슨 버튼을 눌렀는지
  const [selected, setSelected] = useState("");

  const navigate = useNavigate();

  return (
    <ConfigProvider theme={themeGreen}>
      <div>
        <div onClick={() => flipCard(0)} className={Style.behindInfo}>
          <div style={{display: "flex", justifyContent: "space-between"}}>
            <Button type={"primary"} size={"small"}
                    onClick={() => navigate(`/plant/${garden.plant.id}`)}>{garden.plant.name}</Button>
            <LeftOutlined/>
          </div>
          <p
            style={{color: color, fontWeight: "600", marginTop: "0.3rem", fontSize: "0.8rem"}}
            className={`new-line`}>
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
      </div>
    </ConfigProvider>
  )
}

export default TodoCardBehind
