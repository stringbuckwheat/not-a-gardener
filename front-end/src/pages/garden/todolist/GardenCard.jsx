import React, {useState} from "react";
import {Link} from "react-router-dom";
import {CCard, CCardBody, CCol, CRow} from "@coreui/react";
import GardenTag from "./GardenTag";
import getWateringMsg from "../../../utils/function/getWateringMsg";
import GardenCardAction from "./GardenCardAction";
import wateringCodeDesign from "../../../utils/dataArray/wateringCodeDesign";
import WateringCodeIcon from "../../../components/etc/WateringCodeIcon";

/**
 * 메인페이지 할 일 카드
 * @param index
 * @param deleteInTodoList
 * @param garden
 * @param openNotification
 * @returns {JSX.Element}
 * @constructor
 */
const GardenCard = ({
                      index,
                      garden,
                      openNotification,
                    }) => {
  const gardenDetail = garden.gardenDetail;
  const plant = garden.plant;

  let color = "danger";

  if (gardenDetail.wateringCode >= 0) {
    color = wateringCodeDesign[gardenDetail.wateringCode].color;
  }

  const [hovered, setHovered] = useState(false);

  return (
    <div className="parent">
      <div className="child">
        <div
          onMouseEnter={() => setHovered(true)}
          onMouseLeave={() => setHovered(false)}>

          <GardenCardAction
            hovered={hovered}
            plantId={plant.id}
            wateringCode={gardenDetail.wateringCode}
            openNotification={openNotification}
            index={index}
          />
          <Link
            className="no-text-decoration"
            to={`/plant/${plant.id}`}>
            <CCard>
              <CCardBody>
                <CRow className="d-flex align-items-center">
                  <CCol xs={2} className="text-center">
                    <WateringCodeIcon wateringCode={gardenDetail.wateringCode}/>
                  </CCol>
                  <CCol xs={1}></CCol>
                  <CCol xs={8}>
                    <div>
                      <div className={`fs-6 fw-semibold text-black`}>{plant.name}</div>
                      <div className="small text-black new-line">
                        <small>{plant.species}</small></div>
                      <GardenTag className="small" plant={garden}/>
                      <p
                        className={`text-${color} fw-semibold small new-line mt-2 `}>
                        {getWateringMsg(gardenDetail)}
                      </p>
                    </div>
                  </CCol>
                </CRow>
              </CCardBody>
            </CCard>
          </Link>
        </div>
      </div>
    </div>
  )
}

export default GardenCard;
