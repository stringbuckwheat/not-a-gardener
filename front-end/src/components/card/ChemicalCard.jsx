import {CCol, CWidgetStatsF} from "@coreui/react";
import {cilRestaurant, cilFlower, cilMugTea, cilDrop, cilBug, cilCut, cilAnimal} from "@coreui/icons";
import CIcon from '@coreui/icons-react';
import {Link} from "react-router-dom";

/**
 * 비료/살충/살균제 카드
 * @param chemical
 * @returns {JSX.Element}
 * @constructor
 */
const ChemicalCard = ({chemical}) => {
  let color = "";
  let icon = {};

  if (chemical.type === "기본 NPK 비료") {
    color = "primary";
    icon = cilRestaurant;
  } else if (chemical.type === "개화용 비료") {
    color = "warning";
    icon = cilFlower;
  } else if (chemical.type === "미량 원소 비료") {
    color = "success";
    icon = cilMugTea;
  } else if (chemical.type === "살충제/농약") {
    color = "danger";
    icon = cilBug;
  } else if (chemical.type === "살균제/농약") {
    color = "info";
    icon = cilCut;
  } else if (chemical.type === "천적 방제") {
    color = "secondary";
    icon = cilAnimal;
  } else {
    color = "info";
    icon = cilDrop;
  }

  return (
    <CCol md={3} xs={12}>
      <Link
        to={`/chemical/${chemical.chemicalId}`}
        state={chemical}
        className="no-text-decoration">
        <CWidgetStatsF
          className="mb-3"
          color={color}
          icon={<CIcon icon={icon} height={30}/>}
          title={
            <>
              <div>{chemical.type}</div>
              <div className="mt-2"><small>{chemical.period}일에 한 번</small></div>
            </>}
          value={
            <div className="d-flex justify-content-between">
              <div>{chemical.name}</div>
            </div>
          }
        />
      </Link>
    </CCol>
  )
}

export default ChemicalCard;
