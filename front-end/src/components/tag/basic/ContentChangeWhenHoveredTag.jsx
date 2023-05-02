import {Tag} from "antd";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

/**
 * 마우스 후버 시 색, 내용이 바뀌고 link로 연결되는 태그
 * @param color
 * @param to 링크 주소
 * @param defaultContent 기본으로 표시할 내용
 * @param hoveredContent hover 시 표시할 내용
 * @returns {JSX.Element}
 * @constructor
 */
const ContentChangeWhenHoveredTag = ({color, to, defaultContent, hoveredContent}) => {
  const navigate = useNavigate();
  const [isHovered, setIsHovered] = useState(false);

  return (
    <Tag
      color={isHovered ? `${color}-inverse` : color}
      onClick={() => navigate(to)}
      onMouseEnter={() => setIsHovered(!isHovered)}
      onMouseLeave={() => setIsHovered(!isHovered)}
    >
      {isHovered ? hoveredContent : defaultContent}
    </Tag>
  )
}

export default ContentChangeWhenHoveredTag;
