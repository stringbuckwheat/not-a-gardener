import {Tag} from "antd";
import {useState} from "react";

const ClickableTag = ({color, onClick, content}) => {
  const [isHovered, setIsHovered] = useState(false);

  return (
    <Tag
      color={isHovered ? `${color}-inverse` : color}
      onClick={onClick}
      onMouseEnter={() => setIsHovered(!isHovered)}
      onMouseLeave={() => setIsHovered(!isHovered)}
    >
      {content}
    </Tag>
  )
}

export default ClickableTag
