import {CButton} from "@coreui/react";
import React from "react";

const Button = (props) => {
  const {name, size, onClick, color, className, textColor} = props

  return (
    <CButton
      type={"button"}
      size={size}
      onClick={onClick}
      className={`bg-${color} ${className} border-0 text-${textColor}`}>{name}</CButton>
  )
}

export default Button;
