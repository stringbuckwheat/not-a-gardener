import { useTrail, animated } from "@react-spring/web";

export default function Animation() {
  const trailSprings = useTrail(2, {
    from: { transform: "translateY(-5px)" },
    to: { transform: "translateY(5px)" }
  });

  return (
    <>
      {trailSprings.map((spring, index) => {
        const title = ['물주기', '미루기'];

        return (
        <animated.button
          key={index}
          className={"mb-1"}
          style={{
            ...spring,
            border: "none",
            fontWeight: "bold"
          }}
        >{title[index]}</animated.button>
        )
      })}
    </>
  );
}
