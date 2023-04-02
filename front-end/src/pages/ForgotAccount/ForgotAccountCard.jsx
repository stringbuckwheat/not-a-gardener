import {CButton, CCard, CCardBody} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import React from "react";

const ForgotAccountCard = (props) => {
  const {icon, color, onClick, title, buttonSize} = props;
  const iconSize = props.iconSize ? props.iconSize : "8xl";

  return (
    <CCard
      style={{minHeight: "50vh", height: "100%"}}
      className={`bg-${color}`}
      onClick={onClick}>
      <CCardBody className="d-flex align-items-center justify-content-center">
        <div className="text-center">
          <div>
            <CIcon icon={icon} size={iconSize} className={`${color === 'orange' ? "text-beige" : "text-orange"} mb-2`}/>
          </div>
          <CButton style={{border: 'none'}} size={buttonSize}
                   className={`${color === 'orange' ? "bg-beige text-orange" : "bg-orange text-beige"} `}>{title}</CButton>
        </div>
      </CCardBody>
    </CCard>
  )
}

export default ForgotAccountCard;
