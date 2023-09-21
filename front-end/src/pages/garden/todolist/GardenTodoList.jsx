import GardenCard from "./GardenCard";
import React from "react";
import {Row} from "antd";
import {useTrail, animated} from "@react-spring/web";
import {CCol} from "@coreui/react";

/**
 *
 * @param todoList
 * @param deleteInTodoList
 * @param deleteInWaitingListAndTodoList todolist, waitinglist에서 모두 삭제 -- action 콜백함수
 * @param updateGardenAfterWatering
 * @param openNotification
 * @param postponeWatering
 * @returns {JSX.Element}
 * @constructor
 */
const GardenTodoList = ({
                          todoList,
                          deleteInTodoList,
                          deleteInWaitingListAndTodoList,
                          updateGardenAfterWatering,
                          openNotification,
                          postponeWatering
                        }) => {
  const animation = {
    from: {opacity: 0},
    to: {opacity: 1}
  }

  const trailSprings = useTrail(todoList.length, animation);

  return (
    <Row>
      {
        trailSprings.map((spring, index) => (
          <CCol md={3} sm={4} xs={12} className="mb-5" key={`${todoList[index].gardenDetail.wateringCode}-${index}`}>
            <animated.div style={{...spring}} className="parent card-container-item">
              <GardenCard
                postponeWatering={postponeWatering}
                updateGardenAfterWatering={updateGardenAfterWatering}
                index={index}
                garden={todoList[index]}
                deleteInTodoList={deleteInTodoList}
                deleteInWaitingListAndTodoList={deleteInWaitingListAndTodoList}
                openNotification={openNotification}/>
            </animated.div>
          </CCol>
        ))
      }
    </Row>
  )
}

export default GardenTodoList;
