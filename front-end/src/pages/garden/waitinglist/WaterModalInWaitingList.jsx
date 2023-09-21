import {CCol, CModal, CModalBody, CRow} from "@coreui/react";
import {Link} from "react-router-dom";
import {Tag} from "antd";
import GardenCardAction from "../todolist/GardenCardAction";
import React from "react";
import {useSelector} from "react-redux";

/**
 *
 * @param visible
 * @param closeDeleteModal
 * @param clickedPlant
 * @param chemicalList
 * @param openNotification
 * @param handleWaitingList action 후 콜백 함수. todolist, waitinglist에서 삭제한 후 모달 닫기
 * @returns {JSX.Element}
 * @constructor
 */
const WaterModalInWaitingList = ({
                                visible,
                                closeDeleteModal,
                                clickedPlant,
                                openNotification,
                                handleWaitingList
                              }) => {
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
                hovered={true}
                y={20}
                plantId={clickedPlant.id}
                plantName={clickedPlant.name}
                openNotification={openNotification}
                handleWaitingList={handleWaitingList}
              />
            </div>
          </CCol>
        </CRow>
      </CModalBody>
    </CModal>
  )
}

export default WaterModalInWaitingList;
