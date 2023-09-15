import {DeleteOutlined} from "@ant-design/icons";
import {useState} from "react";

const DeleteIconBtn = ({onClick}) => {
  const [isHovered, setIsHovered] = useState(false);
  const handleHover = () => setIsHovered(!isHovered);

  return (
    <DeleteOutlined
      onMouseEnter={handleHover}
      onMouseLeave={handleHover}
      className={isHovered ? "text-orange" : ""}
      onClick={onClick}
    />
  )
}

export default DeleteIconBtn;
