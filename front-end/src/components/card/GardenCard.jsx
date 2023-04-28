import React, {useState} from "react";
import {Link} from "react-router-dom";
import {CCard, CCardBody, CCol, CRow} from "@coreui/react";
import GardenTag from "../../pages/garden/GardenTag";
import getWateringMsg from "../../utils/function/getWateringMsg";
import GardenCardAction from "../../pages/garden/GardenCardAction";
import wateringCodeDesign from "../../utils/dataArray/wateringCodeDesign";
import WateringCodeIcon from "../etc/WateringCodeIcon";

/**
 * 메인페이지 할 일 카드
 * @param index
 * @param deleteInTodoList
 * @param garden
 * @param chemicalList
 * @param openNotification
 * @param updateGardenAfterWatering
 * @param postponeWatering
 * @returns {JSX.Element}
 * @constructor
 */
const GardenCard = ({
                      index,
                      deleteInTodoList,
                      garden,
                      chemicalList,
                      openNotification,
                      updateGardenAfterWatering,
                      postponeWatering
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
            plantNo={plant.plantId}
            plantName={plant.name}
            wateringCode={gardenDetail.wateringCode}
            chemicalList={chemicalList}
            openNotification={openNotification}
            updateGardenAfterWatering={updateGardenAfterWatering}
            postponeWatering={postponeWatering}
            index={index}
            deleteInTodoList={deleteInTodoList}
          />
          <Link
            className="no-text-decoration"
            to={`/plant/${plant.plantNo}`}>
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
                        <small>{`${plant.species} / ${plant.name}`}</small></div>
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
