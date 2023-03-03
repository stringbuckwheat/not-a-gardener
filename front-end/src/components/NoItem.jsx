import { CContainer, CImage, CRow} from "@coreui/react";
import React from "react";
import forNoPlant from "../assets/images/forNoPlant.png";

const NoItem = (props) => {
    const title = props.title;
    const button = props.button;

    return(
        <CContainer fluid className="text-center">
            <CRow className="text-center">
                <h2>{title}</h2>
                <div className="d-grid gap-2 col-6 mx-auto mt-2">
                    {button}
                </div>
            </CRow>
            <CRow >
                <CImage src={forNoPlant} fluid style={{display:'block', width:'100%', height:'100%'}}/>
            </CRow>
        </CContainer>
    )
}

export default NoItem;