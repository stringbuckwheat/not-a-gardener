import React, {useState} from "react";
import {Link} from "react-router-dom";
import {CCard, CCardBody, CCol, CRow} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import GardenTag from "../../pages/garden/GardenTag";
import getWateringMsg from "../../utils/function/getWateringMsg";
import {cilDrop, cilFaceDead, cilHandPointUp, cilSmile, cilSwimming, cilZoom} from "@coreui/icons";
import GardenCardAction from "../../pages/garden/GardenCardAction";

const GardenCard = ({garden, chemicalList, openNotification, updateGardenAfterWatering}) => {
  const gardenDetail = garden.gardenDetail;
  const plant = garden.plant;

  // 음수           0            1          2         3        4
  // 물주기 놓침   물주기 정보 부족    물주기     체크하기     놔두세요    오늘 물 줌

  const designs = [
    {
      color: "primary",
      icon: cilZoom
    },
    {
      color: "primary",
      icon: cilDrop
    },
    {
      color: "warning",
      icon: cilHandPointUp
    },
    {
      color: "success",
      icon: cilSmile,
    },
    {
      color: "success",
      icon: cilSwimming
    }
  ]

  let color = "danger";
  let icon = cilFaceDead;

  if (gardenDetail.wateringCode >= 0) {
    color = designs[gardenDetail.wateringCode].color;
    icon = designs[gardenDetail.wateringCode].icon;
  }

  const [hovered, setHovered] = useState(false);

  return (
    <div className="parent">
      <div className={"child"}>
        <div
          onMouseEnter={() => setHovered(true)}
          onMouseLeave={() => setHovered(false)}>
          {/*{hovered*/}
          {/*  ? <GardenCardAction*/}
          {/*    hovered={hovered}*/}
          {/*    plantNo={plant.plantNo}*/}
          {/*    plantName={plant.plantName}*/}
          {/*    chemicalList={chemicalList}*/}
          {/*  />*/}
          {/*: <></>}*/}

          <GardenCardAction
              hovered={hovered}
              plantNo={plant.plantNo}
              plantName={plant.plantName}
              chemicalList={chemicalList}
              openNotification={openNotification}
              updateGardenAfterWatering={updateGardenAfterWatering}/>

          <Link
            className="no-text-decoration"
            to={`/plant/${plant.plantNo}`}>
            <CCard>
              <CCardBody>
                <CRow className="d-flex align-items-center">
                  <CCol xs={2} className="text-center">
                    <CIcon className={`me-3 text-${color}`} icon={icon} height={45}/>
                  </CCol>
                  <CCol xs={1}></CCol>
                  <CCol xs={8}>
                    <div>
                      <div className={`fs-6 fw-semibold text-black`}>{plant.plantName}</div>
                      <div className="small text-black new-line">
                        <small>{`${plant.plantSpecies} / ${plant.placeName}`}</small></div>
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
