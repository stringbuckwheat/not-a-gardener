import { CCol, CWidgetStatsF } from "@coreui/react";
import { cilRestaurant, cilFlower, cilMugTea, cilDrop, cilTrash, cilBug, cilCut, cilAnimal } from "@coreui/icons";
import CIcon from '@coreui/icons-react';
import { Link } from "react-router-dom";


const ChemicalCard = (props) => {
    const chemical = props.chemical;

    let color = "";
    let icon = {};

    if (chemical.chemicalType === "기본 NPK 비료") {
        color = "primary";
        icon = cilRestaurant;
    } else if (chemical.chemicalType === "개화용 비료") {
        color = "warning";
        icon = cilFlower;
    } else if (chemical.chemicalType === "미량 원소 비료") {
        color = "success";
        icon = cilMugTea;
    } else if (chemical.chemicalType === "살충제/농약") {
        color = "danger";
        icon = cilBug;
    } else if (chemical.chemicalType === "살균제/농약") {
        color = "info";
        icon = cilCut;
    } else if (chemical.chemicalType === "천적 방제") {
        color = "secondary";
        icon = cilAnimal;
    } else {
        color = "info";
        icon = cilDrop;
    }

    return (
        <CCol md={3} xs={12}>
            <Link to={`/chemical/${chemical.chemicalNo}`} state={chemical} style={{ textDecoration: "none" }}>
                <CWidgetStatsF
                    className="mb-3"
                    color={color}
                    icon={<CIcon icon={icon} height={30} />}
                    title={
                        <>
                            <div>{chemical.chemicalType}</div>
                            <div className="mt-2"><small>{chemical.chemicalPeriod}일에 한 번</small></div>
                        </>}
                    value={
                        <>
                            <div className="d-flex justify-content-between">
                                <div>{chemical.chemicalName}</div>
                            </div>
                        </>
                    }
                />
            </Link>
        </CCol>
    )
}

export default ChemicalCard;
