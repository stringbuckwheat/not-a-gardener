import {CCol, CModal, CModalBody, CRow} from "@coreui/react";
import {Link} from "react-router-dom";
import {Tag} from "antd";
import GardenCardAction from "./GardenCardAction";
import React from "react";

const WateredInGardenModal = ({visible, closeDeleteModal, clickedPlant, chemicalList, openNotification, afterFirstWatering}) => {
  return (
    <CModal alignment="center" visible={visible} onClose={closeDeleteModal}>
      <CModalBody>
        <CRow className="d-flex justify-content-center">
          <CCol md={10} className="">
            <h5 className="text-center text-dark">
              <Link to={`/plant/${clickedPlant.id}`}
                    className="no-text-decoration text-teal">{clickedPlant.name}</Link>
              의 물주기를 추가해주세요
            </h5>
            <div className="text-center mb-4">
              {
                clickedPlant.species ? <Tag>{clickedPlant.species}</Tag> : <></>
              }
              <Tag>{clickedPlant.name}</Tag>
              <Tag>{clickedPlant.createDate}부터 함께</Tag>
            </div>
            <div className="text-center mb-4">
              <GardenCardAction
                y={20}
                hovered={true}
                plantId={clickedPlant.id}
                plantName={clickedPlant.name}
                chemicalList={chemicalList}
                openNotification={openNotification}
                updateGardenAfterWatering={afterFirstWatering}/>
            </div>
          </CCol>
        </CRow>
      </CModalBody>
    </CModal>
  )
}

export default WateredInGardenModal;
