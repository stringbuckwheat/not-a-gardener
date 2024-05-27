import {useTrail, animated} from "@react-spring/web";
import {Col, Row} from "antd";
import {CaretRightOutlined, StepBackwardOutlined, StepForwardOutlined} from "@ant-design/icons";
import {useDispatch} from "react-redux";
import updateData from "../../../../api/backend-api/common/updateData";
import getAfterWateringMsg from "../../../../utils/function/getAfterWateringMsg";
import {useState} from "react";

/**
 * 메인 페이지 할일 카드에 클릭시 시 나타날 버튼 세가지
 * @param setSelected // 어떤 버튼을 선택했는지
 * @param y
 * @param plantId
 * @param openNotification
 * @param index
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
      msg = getAfterWateringMsg(res.afterWateringCode);
    }

    openNotification(msg);
  }

  const onClickPostpone = async () => {
    const res = await updateData(`/garden/${plantId}/watering/postpone`, null);
    dispatch({type: 'updateTodoList', payload: {index, res}});
  }

  const icon = {fontSize: "2rem"}

  const buttons = [
    {
      title: "안 말랐어요",
      icon: <StepBackwardOutlined style={icon}/>,
      onClick: onClickNotDry
    },
    {
      title: "물을 줬어요!",
      icon: <CaretRightOutlined style={icon}/>,
      onClick: () => setSelected('watered')
    },
    {
      title: "미룰래요",
      icon: <StepForwardOutlined style={icon}/>,
      onClick: onClickPostpone
    }
  ]

  const trailSprings = useTrail(buttons.length, {
    from: {transform: `translateY(-5px)`},
    to: {transform: `translateY(5px)`}
  });

  return (
    <Row style={{marginTop: "1.5rem"}}>
      {trailSprings.map((spring, index) => (
        <Col
          xs={8}
          style={{textAlign: "center"}}
          key={index}
          onClick={buttons[index].onClick}
          onMouseEnter={() => mouseEnter(index)}
          onMouseLeave={mouseLeave}>
          <animated.button
            className={`animated-btn`}
            style={{
              ...spring,
              color: hovered == index ? "black" : "#4f5d73"
            }}
          >
            <div style={{margin: "0 1rem"}}>
              {buttons[index].icon}
              {/* TODO class 수정*/}
              <p
                style={{fontSize: "0.7rem", whiteSpace: "nowrap"}}
                className={`${hovered == index ? "text-black bold" : "text-dark"}`}>
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
