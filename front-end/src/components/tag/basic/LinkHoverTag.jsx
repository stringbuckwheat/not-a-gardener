import {Tag} from "antd";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

const LinkHoverTag = ({color, to, content}) => {
  const navigate = useNavigate();
  const [isHovered, setIsHovered] = useState(false);

  return (
    <Tag
      color={isHovered ? `${color}-inverse` : color}
      onClick={() => navigate(to)}
      onMouseEnter={() => setIsHovered(!isHovered)}
      onMouseLeave={() => setIsHovered(!isHovered)}
    >
      {content}
    </Tag>
  )
}

export default LinkHoverTag;
