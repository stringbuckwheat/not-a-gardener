import {useTrail, animated} from "@react-spring/web";
import {Button} from "antd";
import React, {useState} from "react";

const GardenCardAnimatedButton = ({plantNo, chemicalList, plantName, setSelected}) => {

  const buttons = [
    {
      title: "오늘 물을 줬어요!",
      className: "text-white bg-info",
      onClick: () => setSelected('watered')
    },
    {
      title: "화분이 안 말랐어요",
      className: "text-white bg-teal",
      onClick: () => setSelected('notDry'),
    },
    {
      title: "귀찮아요. 내일 줄래요",
      className: "text-white bg-dark",
      onClick: () => setSelected('lazy'),
    }
  ]

  const trailSprings = useTrail(buttons.length, {
    from: {transform: "translateY(-5px)"},
    to: {transform: "translateY(5px)"}
  });

  return (
    <>
      {trailSprings.map((spring, index) => (
        <animated.div
          key={index}
          className="mb-1 animated-btn"
          style={{
            ...spring
          }}
        >
          <Button
            className={buttons[index].className}
            size="small"
            onClick={buttons[index].onClick}>{buttons[index].title}</Button>
        </animated.div>
      ))}
    </>
  );
}

export default GardenCardAnimatedButton
