import {Tag} from "antd";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

/**
 * 마우스 후버 시 색이 바뀌고 link로 연결되는 태그
 * @param color
 * @param to 보낼 주소
 * @param content
 * @returns {JSX.Element}
 * @constructor
 */
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
