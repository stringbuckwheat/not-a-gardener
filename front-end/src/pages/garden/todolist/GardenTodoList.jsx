import React from "react";
import {Col, Row} from "antd";
import {useTrail, animated} from "@react-spring/web";
import {useSelector} from "react-redux";
import Empty from "../../../components/empty/Empty";
import {useNavigate} from "react-router-dom";
import GButton from "../../../components/button/GButton";
import ToDoCard from "./todo-card/ToDoCard";
import Style from "../../../components/card/ItemCard.module.scss";

/**
 *
 * @param todoList
 * @param openNotification
 * @returns {JSX.Element}
 * @constructor
 */
const GardenTodoList = ({openNotification}) => {
  const todoList = useSelector(state => state.gardens.todoList);
  console.log("todoList", todoList);
  const navigate = useNavigate();

  const animation = {
    from: {opacity: 0},
    to: {opacity: 1}
  }

  const trailSprings = useTrail(todoList.length, animation);

  return todoList.length == 0 ? (<Empty title="목마른 식물이 없어요"/>) : (
    <>
      <div style={{display: "flex", justifyContent: "flex-end", margin: "0.5rem 0"}}>
        <GButton color="teal" className="float-end" onClick={() => navigate('/plant')}>전체 식물 보기</GButton>
      </div>
      <Row>
        {
          trailSprings.map((spring, index) => (
            <Col xs={24} sm={12} md={8} lg={6} style={{marginBottom: "1rem"}}
                 key={`${todoList[index].gardenDetail.wateringCode}-${index}`}>
              <animated.div style={{...spring}}>
                <ToDoCard
                  index={index}
                  garden={todoList[index]}
                  openNotification={openNotification}/>
              </animated.div>
            </Col>
          ))
        }
      </Row>
    </>
  )
}

export default GardenTodoList;
