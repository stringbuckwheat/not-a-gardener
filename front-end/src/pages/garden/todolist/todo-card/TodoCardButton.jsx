import {useTrail, animated} from "@react-spring/web";
import {Col, Row} from "antd";
import React, {useState} from "react";
import {CaretRightOutlined, StepBackwardOutlined, StepForwardOutlined} from "@ant-design/icons";
import {useDispatch} from "react-redux";
import updateData from "../../../../api/backend-api/common/updateData";
import getWateringNotificationMsg from "../../../../utils/function/getWateringNotificationMsg";

/**
 * 메인 페이지 할일 카드에 클릭시 시 나타날 버튼 세가지
 * @param setSelected // 어떤 버튼을 선택했는지
 * @param y
 * @param plantId
 * @param openNotification
 * @param index
 * @param postponeWatering 물주기 미룬 후 콜백함수. 객체 내부 뜯어서 wateringCode만 교체
 * @param wateringCode 이미 미뤘는데 또 미루겠다고 하면 return (6이면 리턴)
 * @returns {JSX.Element}
 * @constructor
 */
const TodoCardButton = ({
                          setSelected,
                          plantId,
                          openNotification,
                          index,
                        }) => {
  const dispatch = useDispatch();

  const onClickNotDry = async () => {
    // 안 말랐어요

    // waitinglist에서는... 그냥 유지
    const res = await updateData(`/garden/${plantId}/watering/not-dry`, null);
    console.log("not-dry res", res);

    // 현재 todoList에서 삭제
    dispatch({type: 'deleteInTodoList', payload: plantId});

    // res가 없는 경우 -> 한 번도 물 준 적 없는 경우
    let msg = {
      title: "관찰 결과를 알려줘서 고마워요",
      content: "앞으로도 함께 키워요"
    }

    // 물주기가 늘어났어요 메시지
    if (res) {
      msg = getWateringNotificationMsg(res.afterWateringCode);
    }

    openNotification(msg);
  }

  const onClickPostpone = async () => {
    const res = await updateData(`/garden/${plantId}/watering/postpone`, null);
    dispatch({type: 'updateTodoList', payload: {index, res}});
  }

  const buttons = [
    {
      title: "안 말랐어요",
      icon: <StepBackwardOutlined className={"font-2rem"}/>,
      onClick: onClickNotDry
    },
    {
      title: "물을 줬어요!",
      icon: <CaretRightOutlined className={"font-2rem"}/>,
      onClick: () => setSelected('watered')
    },
    {
      title: "미룰래요",
      icon: <StepForwardOutlined className={"font-2rem"}/>,
      onClick: onClickPostpone
    }
  ]

  const trailSprings = useTrail(buttons.length, {
    from: {transform: `translateY(-5px)`},
    to: {transform: `translateY(5px)`}
  });

  return (
    <Row className={"mt"}>
      {trailSprings.map((spring, index) => (
        <Col
          xs={8}
          className={"text-center"}
          key={index}
          onClick={buttons[index].onClick}>
          <animated.button
            className={`mb-1 animated-btn water-btn`}
            style={{
              ...spring,
            }}
          >
            <div className={"water-btn-m"}>
              {buttons[index].icon}
              <p
                className={`water-btn-text`}>
                {buttons[index].title}
              </p>
            </div>
          </animated.button>
        </Col>
      ))}
    </Row>
  );
}

export default TodoCardButton
