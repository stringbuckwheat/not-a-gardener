import {useTrail, animated} from "@react-spring/web";
import {Row} from "antd";
import React, {useState} from "react";
import {CaretRightOutlined, StepBackwardOutlined, StepForwardOutlined} from "@ant-design/icons";
import {CCol,} from "@coreui/react";
import updateData from "../../api/backend-api/common/updateData";
import getWateringNotificationMsg from "../../utils/function/getWateringNotificationMsg";

/**
 * 메인 페이지 할일 카드에 마우스를 올릴 시 나타날 버튼 세가지
 * @param setSelected
 * @param y
 * @param plantNo
 * @param openNotification
 * @param index
 * @param deleteInTodoList
 * @param postponeWatering
 * @param wateringCode
 * @returns {JSX.Element}
 * @constructor
 */
const GardenCardAnimatedButton = ({
                                    setSelected,
                                    y,
                                    plantNo,
                                    openNotification,
                                    index,
                                    deleteInTodoList,
                                    postponeWatering,
                                    wateringCode
                                  }) => {
  const [hovered, setHovered] = useState(-1);

  const mouseEnter = (index) => setHovered(() => index);
  const mouseLeave = () => setHovered(-1);

  const onClickNotDry = async () => {
    // 안 말랐어요

    // 가든 카드 바꿔끼우는데 다른 코드가 필요하다! -> watering code 추가(물주기 늘어나는 중)
    // waitinglist에서는... 그냥 유지
    const res = await updateData(`/garden/${plantNo}/watering/not-dry`, null);

    // 현재 todoList에서 삭제
    deleteInTodoList(index);

    let msg = {
      title: "관찰 결과를 알려줘서 고마워요",
      content: "앞으로도 함께 키워요"
    }

    // 물주기가 늘어났어요 메시지
    if (res) {
      msg = getWateringNotificationMsg(res.afterWateringCode);
    }

    // res가 없는 경우 -> 한 번도 물 준 적 없는 경우
    openNotification(msg);
  }

  const onClickPostpone = async () => {
    // 오늘 이미 미뤘는데 오늘 또 미루겠다고 하면 동작X
    if(wateringCode == 6){
      return;
    }

    const res = await updateData(`/garden/${plantNo}/watering/postpone`, null);
    postponeWatering(index, res);
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
    from: {transform: `translateY(${y * -1}px)`},
    to: {transform: `translateY(${y}px)`}
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

export default GardenCardAnimatedButton
