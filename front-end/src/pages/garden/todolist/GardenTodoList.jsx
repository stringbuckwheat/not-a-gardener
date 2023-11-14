import GardenCard from "./GardenCard";
import React from "react";
import {Col, Row} from "antd";
import {useTrail, animated} from "@react-spring/web";
import {CCol} from "@coreui/react";
import {useSelector} from "react-redux";
import Empty from "../../../components/empty/Empty";
import {Link} from "react-router-dom";
import GButton from "../../../components/button/GButton";

/**
 *
 * @param todoList
 * @param openNotification
 * @returns {JSX.Element}
 * @constructor
 */
const GardenTodoList = ({
                          openNotification,
                        }) => {
  const todoList = useSelector(state => state.gardens.todoList);

  const animation = {
    from: {opacity: 0},
    to: {opacity: 1}
  }

  const trailSprings = useTrail(todoList.length, animation);

  return todoList.length == 0 ? (<Empty title="목마른 식물이 없어요"/>) : (
    <>
      <Link to={"/plant"} className="d-flex justify-content-end mb-4 mt-3">
        <GButton color="teal" className="float-end">전체 식물 보기</GButton>
      </Link>
      <div className="mt-2">
        <Row>
          {
            trailSprings.map((spring, index) => (
              <Col md={6} sm={8} xs={24} className="mb-5"
                    key={`${todoList[index].gardenDetail.wateringCode}-${index}`}>
                <animated.div style={{...spring}} className="parent card-container-item">
                  <GardenCard
                    index={index}
                    garden={todoList[index]}
                    openNotification={openNotification}/>
                </animated.div>
              </Col>
            ))
          }
        </Row>
      </div>
    </>
  )
}

export default GardenTodoList;
