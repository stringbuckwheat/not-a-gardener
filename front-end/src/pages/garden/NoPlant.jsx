import { CContainer, CImage, CRow, CCol, CButton } from "@coreui/react";
import { Link } from "react-router-dom";
import React from "react";
import forNoPlant from "./forNoPlant.png";

const NoPlant = () => {
    console.log("no plant page")

    return(
        <CContainer fluid className="text-center">
            <CRow className="text-center">
                <h2>아직 정원에 아무도 없네요!</h2>
                <div className="d-grid gap-2 col-6 mx-auto mt-2">
                    <Link to="/garden/addPlant">
                        <CButton color="success" size="lg" variant="outline" shape="rounded-pill">식물 추가하기</CButton>
                    </Link>
                </div>
            </CRow>
            <CRow >
                <CImage src={forNoPlant} fluid style={{display:'block', width:'100%', height:'100%'}}/>
            </CRow>
        </CContainer>
    )
}

export default NoPlant;