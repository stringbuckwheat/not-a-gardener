import React, { useState, useEffect }  from 'react'
import { Link } from 'react-router-dom'
import {
    CRow,
    CCol,
    CDropdown,
    CDropdownMenu,
    CDropdownItem,
    CDropdownToggle,
    CWidgetStatsA,
    CContainer,
  } from '@coreui/react'
import CIcon from '@coreui/icons-react'
import { cilOptions } from '@coreui/icons'
import WaterModal from './WaterModal'
import Plant from '../plant/Plant'
import DeletePlant from '../plant/DeletePlant'

const Garden = (props) => {
    // 물주기 모달 표시 여부
  const [visible, setVisible] = useState(false);

  // 클릭한 식물의 plantNo
  const [clickedPlant, setClickedPlant] = useState(0);

  // 삭제 모달
  const [deleteVisible, setDeleteVisible ] = useState(false);

  const openModal = (plantNo) => {
    setVisible(!visible);
    setClickedPlant(plantNo);
  }

  const closeModal = () => {
    setVisible(false);
  }

  const deletePlant = (plantNo) => {
    setDeleteVisible(true);
    setClickedPlant(plantNo);
  }

  const closeDeleteModal = () => {
    setDeleteVisible(false)
  }

  // 삭제 시 plantNo가 일치하지 않는 원소만 추출해서 새로운 배열 만듦
   const onRemove = () => {
//     setPlantList(plantList.filter(plant => plant.plantNo !== clickedPlant))
   }

  return (
    // <div>{plantList.length == 0 ? "등록된 식물이 없습니다" : "밑으로 리스트"}</div>
      <CContainer>
        <CRow>
         {props.plantList.map((plant, idx) => {
            const color = ["primary", "warning", "danger", "success"];

            let message = "";
            let periodMessage = `이 식물의 평균 물주기는 ${plant.averageWateringPeriod}일입니다.`

            if(plant.wateringCode == 0){
              message = "이 식물은 목이 말라요!";

              if(plant.fertilizingCode == 0){
                message += "맹물을 주세요!";
              } else {
                message += "비료를 주세요!";
              }

            } else if(plant.wateringCode == 1) {
              message = "최근 물주기 하루 전입니다. 흙이 말랐는지 확인해보세요! 말랐다면 "

              if(plant.fertilizingCode == 0){
                message += "맹물을 주세요!";
              } else {
                message += "비료를 주세요!";
              }

            } else if(plant.wateringCode == 2) {
              message = "물 줄 날짜를 놓쳤어요! 비료 절대 안 됨!"
            } else if(plant.wateringCode == 3) {
              message = "놔두세요. 그냥 관상하세요.";
            } else if(plant.wateringCode == 4) {
              message = "아직 물주기 정보가 부족해요. 우리 함께 매일 체크해보아요!";
              periodMessage = "";
            } else if(plant.wateringCode == 5){
              message = "오늘 물 마신 식물!";
            }

            const modifyUrl = `/garden/modify-plant/${plant.plantNo}`;


           return (
              <CCol sm={6} lg={3}>
                <WaterModal visible={visible} clickedPlant={clickedPlant} closeModal={closeModal} />
                <CWidgetStatsA
                  className="mb-4"
                  color={color[plant.wateringCode % 4]} // 일단 4로 나눈 나머지로 해결
                  value={
                    <div onClick={() => {openModal(plant.plantNo)}}>
                      <span role="img" aria-label="herb">🌿 </span>
                          {plant.plantName}{' '}
                      <span role="img" aria-label="herb">🌿</span>
                      <div className="fs-6 fw-normal">
                        <div>{plant.plantSpecies}</div>
                        <div>
                            {message}
                        </div>
                      </div>
                    </div>
                  }
                  action={
                    <CDropdown alignment="end">
                      <CDropdownToggle color="transparent" caret={false} className="p-0">
                        <CIcon icon={cilOptions} className="text-high-emphasis-inverse" />
                      </CDropdownToggle>
                      <CDropdownMenu>
                        <Link to={modifyUrl} component={Plant}>
                          <CDropdownItem>상세 정보</CDropdownItem>
                        </Link>
                        <div onClick={() => {deletePlant(plant.plantNo)}}>
                          <CDropdownItem>삭제</CDropdownItem>
                          <DeletePlant deleteVisible={deleteVisible} clickedPlant={clickedPlant} closeDeleteModal={closeDeleteModal} onRemove={onRemove}/>
                        </div>
                        <CDropdownItem disabled>Disabled action</CDropdownItem>
                      </CDropdownMenu>
                    </CDropdown>
                  }
                  icon={<CIcon icon={cilOptions} className="text-high-emphasis-inverse" />}
                />
              </CCol>
            )
        })}
      </CRow>
    </CContainer>
    )
}

export default Garden;
