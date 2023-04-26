import {Button} from "antd";
import React from "react";

const GButton = ({color, className, onClick, size, children}) => {
  return (
    <Button
      className={`bg-${color} text-white ${className}`}
      onClick={onClick}
    size={size}>{children}</Button>
  )
}

export default GButton
