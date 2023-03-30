import {useTrail, animated} from "@react-spring/web";
import {CButton} from "@coreui/react";

const GardenCardAnimatedButton = (props) => {
  const {setIsWaterFormOpen, setIsPostponeFormOpen} = props;

  const trailSprings = useTrail(2, {
    from: {transform: "translateY(-5px)"},
    to: {transform: "translateY(5px)"}
  });

  const water = () => {
    console.log("water 폼 오픈")
    setIsWaterFormOpen(true);
  }

  const postpone = () => {
    console.log("미루기 폼 오픈");
    setIsPostponeFormOpen(true);
  }

  const title = ['물주기', '미루기'];
  const onClicks = [water,];

  return (
    <>
      {trailSprings.map((spring, index) => (
        <animated.button
          key={index}
          className="mb-1 animated-btn"
          style={{
            ...spring
          }}
        >
          <CButton size="sm" onClick={onClicks[index]}>{title[index]}</CButton>
        </animated.button>
      ))}
    </>
  );
}

export default GardenCardAnimatedButton
