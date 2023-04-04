import GardenCard from "../../components/card/GardenCard";
import React from "react";
import {notification, Row} from "antd";
import getWateringNotificationMsg from "../../utils/function/getWateringNotificationMsg";
import {useTrail, animated} from "@react-spring/web";
import {CCol} from "@coreui/react";

const GardenTodoList = ({todoList, chemicalList, updateGardenAfterWatering}) => {
  // 식물 상태 업데이트 이후 메시지 띄우기
  const [api, contextHolder] = notification.useNotification();

  const openNotification = (msg) => {

    api.open({
      message: msg.title,
      description: msg.content,
      duration: 4,
    });
  };

  const animation = {
    from: {opacity: 0},
    to: {opacity: 1}
  }

  const trailSprings = useTrail(todoList.length, animation);

  return (
    <>
      {contextHolder}
      <Row>
        {
          trailSprings.map((spring, index) => (
            <CCol md={3} sm={4} xs={12} className="mb-4" key={index}>
              <animated.div style={{...spring,}} className="parent card-container-item">
                <GardenCard
                  updateGardenAfterWatering={updateGardenAfterWatering}
                  garden={todoList[index]}
                  chemicalList={chemicalList}
                  openNotification={openNotification}/>
              </animated.div>
            </CCol>
          ))
        }
      </Row>
    </>
  )

}

export default GardenTodoList;
