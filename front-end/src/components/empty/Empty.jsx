import {CContainer, CImage, CRow} from "@coreui/react";
import forNoPlant from "../../assets/images/forNoPlant.png";
import React from "react";

// TODO ?? garden 리팩토링하고 없앨 수 있나?
const Empty = ({title, button}) => {
  return(
    <CContainer fluid className="text-center">
      <CRow className="text-center mt-5">
        <h2>
          <div className="vertical-align-middle">{title}</div></h2>
        <div className="d-grid gap-2 col-6 mx-auto mt-2">
          {button}
        </div>
      </CRow>
      <CRow className="d-flex justify-content-center">
        <CImage
          style={{maxHeight: "60vh", width: "auto"}}
          src={forNoPlant}
          fluid/>
      </CRow>
    </CContainer>
  )
}

export default Empty
