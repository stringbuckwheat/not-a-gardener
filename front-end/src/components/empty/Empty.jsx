import forNoPlant from "../../assets/images/forNoPlant.png";
import React from "react";

const Empty = ({title,}) => {
  return (
    <div fluid style={{textAlign: "center"}}>
      <h2 style={{marginTop: "3rem"}}>{title}</h2>
      <img src={forNoPlant} style={{width: "95%"}}/>
    </div>
  )
}

export default Empty
