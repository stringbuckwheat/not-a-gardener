import {Tag} from "antd";
import {useState} from "react";

/**
 * mouse hover 시 색이 바뀌는 태그
 * @param color
 * @param onClick
 * @param content
 * @returns {JSX.Element}
 * @constructor
 */
const ClickableTag = ({color, onClick, content}) => {
  const [isHovered, setIsHovered] = useState(false);

  return (
    <Tag
      style={{margin: "0.2rem"}}
      color={isHovered ? `${color}-inverse` : color}
      onClick={onClick}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
    >
      {content}
    </Tag>
  )
}

export default ClickableTag
