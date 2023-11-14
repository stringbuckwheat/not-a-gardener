import {useTrail, animated} from "@react-spring/web";
import {Row} from "antd";
import React, {useState} from "react";
import {CaretRightOutlined, StepBackwardOutlined, StepForwardOutlined} from "@ant-design/icons";
import {CCol,} from "@coreui/react";
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

  const [hovered, setHovered] = useState(-1);
  const dispatch = useDispatch();

  const mouseEnter = (index) => setHovered(() => index);
  const mouseLeave = () => setHovered(-1);

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

  // #4169E1 라벤더
  const style = {fontSize: "2vw"}

  const buttons = [
    {
      title: "안 말랐어요",
      icon: <StepBackwardOutlined style={style}/>,
      onClick: onClickNotDry
    },
    {
      title: "물을 줬어요!",
      icon: <CaretRightOutlined style={style}/>,
      onClick: () => setSelected('watered')
    },
    {
      title: "미룰래요",
      icon: <StepForwardOutlined style={style}/>,
      onClick: onClickPostpone
    }
  ]

  const trailSprings = useTrail(buttons.length, {
    from: {transform: `translateY(-5px)`},
    to: {transform: `translateY(5px)`}
  });

  return (
    <Row className="mb-1">
      {trailSprings.map((spring, index) => (
        <CCol
          xs={4}
          key={index}
          className={"text-center"}
          onClick={buttons[index].onClick}
          onMouseEnter={() => mouseEnter(index)}
          onMouseLeave={mouseLeave}>
          <animated.button
            className={`mb-1 animated-btn ${hovered == index ? "text-black" : "text-dark"}`}
            style={{
              ...spring
            }}
          >
            {buttons[index].icon}
            <div
              style={{fontSize: "0.8em"}}
              className={`${hovered == index ? "text-black bold" : "text-dark"}`}>
              {buttons[index].title}
            </div>
          </animated.button>
        </CCol>
      ))}
    </Row>
  );
}

export default TodoCardButton
