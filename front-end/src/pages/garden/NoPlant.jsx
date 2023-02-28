import { CContainer, CImage, CRow, CCol, CButton } from "@coreui/react";
import { Link } from "react-router-dom";
import React from "react";
import forNoPlant from "../../assets/images/forNoPlant.png";
import AddPlantButton from "src/components/button/AddPlantButton";

const NoPlant = () => {
    console.log("no plant page")

    return(
        <CContainer fluid className="text-center">
            <CRow className="text-center">
                <h2>아직 정원에 아무도 없네요!</h2>
                <div className="d-grid gap-2 col-6 mx-auto mt-2">
                    <AddPlantButton size="lg" />
                </div>
            </CRow>
            <CRow >
                <CImage src={forNoPlant} fluid style={{display:'block', width:'100%', height:'100%'}}/>
            </CRow>
        </CContainer>
    )
}

export default NoPlant;