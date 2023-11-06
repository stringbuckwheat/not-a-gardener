import {CCol, CModal, CModalBody, CRow} from "@coreui/react";
import {Link} from "react-router-dom";
import {Button, Select, Space, Tag} from "antd";
import GardenCardAction from "../todolist/GardenCardAction";
import React, {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import CIcon from "@coreui/icons-react";
import {cilDrop} from "@coreui/icons";
import postData from "../../../api/backend-api/common/postData";
import getWateringNotificationMsg from "../../../utils/function/getWateringNotificationMsg";

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
                                   closeModal,
                                   clickedPlant,
                                   openNotification,
                                 }) => {
  const dispatch = useDispatch();
  const chemicals = useSelector(state => state.chemicals.forSelect);
  const [chemicalId, setChemicalId] = useState(0);

  const submitWatering = async () => {
    console.log("WaterModalInWaitingList clickedPlant", clickedPlant);

    try {
      const data = {
        plantId: clickedPlant.id,
        chemicalId,
        wateringDate: new Date().toISOString().split('T')[0]
      }

      const res = await postData(`/garden/${clickedPlant.id}/watering`, data);
      console.log("submit watering", res);

      dispatch({type: 'deleteInWaitingList', payload: clickedPlant.id});

      // 메시지 띄우기
      const msg = getWateringNotificationMsg(res.wateringMsg.afterWateringCode);
      openNotification(msg);

      setSelected("");
    } catch (e) {
      if (e.code == "B005") {
        alert(e.message);
      }
    }
  }

  return (
    <CModal alignment="center" visible={visible} onClose={closeModal}>
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
              <Space>
                <Space className="mb-1">
                  <CIcon icon={cilDrop} className="text-info"/>
                  <Select options={chemicals} defaultValue={0} style={{width: 120}}
                          onChange={(value) => setChemicalId(value)}/>
                  <span>을 줬어요</span>
                </Space>
                <Space>
                  <Button onClick={submitWatering} className="bg-info text-white" size="small">제출</Button>
                </Space>
              </Space>
            </div>
          </CCol>
        </CRow>
      </CModalBody>
    </CModal>
  )
}

export default WaterModalInWaitingList;
