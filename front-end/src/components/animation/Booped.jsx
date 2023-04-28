import {useEffect, useState} from "react";

/**
 * 이스터에그로 넣은 인사하는 모션 이모티콘
 * @param rotation
 * @param timing
 * @param children 인사시킬 요소
 * @returns {JSX.Element}
 * @constructor
 */
const Booped = ({ rotation = 0, timing = 150, children }) => {
  const [isBooped, setIsBooped] = useState(false);

  const style = {
    display: 'inline-block',
    backfaceVisibility: 'hidden',
    transform: isBooped
      ? `rotate(${rotation}deg)`
      : `rotate(0deg)`,
    transition: `transform ${timing}ms`,
  };

  useEffect(() => {
    if (!isBooped) {
      return;
    }
    const timeoutId = window.setTimeout(() => {
      setIsBooped(false);
    }, timing);

    return () => {
      window.clearTimeout(timeoutId);
    };

  }, [isBooped, timing]);

  const trigger = () => {
    setIsBooped(true);
  };

  return (
    <span onMouseEnter={trigger} style={style}>
      {children}
    </span>
  );
}

export default Booped;
