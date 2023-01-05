import React, { useState, useEffect }  from 'react'
import {
  CRow,
  CCol,
  CDropdown,
  CDropdownMenu,
  CDropdownItem,
  CDropdownToggle,
  CWidgetStatsA,
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import { cilOptions } from '@coreui/icons'
import authAxios from './requestInterceptor'
import WaterModal from './WaterModal'

const GardenMain = () => {
  console.log("GardenMain start");

  const [plantList, setPlantList] = useState([{
      plantNo: ''
      , plantName: ''
      , averageWateringPeriod: ''
      , wateringCode: ''
      , fertilizingCode: ''
  }]);

  // 백엔드에서 식물 리스트를 받아온다
  useEffect(() => {
    authAxios.get("/garden", "")
        .then((res) => {
          console.log("res.data");
          console.log(res.data);
          setPlantList(res.data);
          })
        .catch(error => console.log(error))
  }, [])

  const [visible, setVisible] = useState(false);
  const [clickedPlant, setClickedPlant] = useState(0);

  const onClick = (plantNo) => {
    setVisible(!visible);
    setClickedPlant(plantNo);
  }

  const closeModal = () => {
    setVisible(false);
  }

  const [ wateringMsg, setAfterWateringMsg ] = useState("");

  return (
  <>
      <CRow>
       {plantList.map((plant, idx) => {
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
          }


         return (
            <CCol sm={6} lg={3}>
              <WaterModal visible={visible} clickedPlant={clickedPlant} closeModal={closeModal}/>
              <CWidgetStatsA
                onClick={() => {onClick(plant.plantNo)}}
                className="mb-4"
                color={color[plant.wateringCode % 4]} // 일단 4로 나눈 나머지로 해결
                value={
                  <>
                    <span role="img" aria-label="herb">🌿 </span>
                        {plant.plantName}{' '}
                    <span role="img" aria-label="herb">🌿</span>

                    <div className="fs-6 fw-normal">
                      <div>{plant.plantSpecies}</div>
                      <div>
                        {periodMessage}
                      </div>
                    </div>
                  </>
                }
                title={message}
                action={
                  <CDropdown alignment="end">
                    <CDropdownToggle color="transparent" caret={false} className="p-0">
                      <CIcon icon={cilOptions} className="text-high-emphasis-inverse" />
                    </CDropdownToggle>
                    <CDropdownMenu>
                      <CDropdownItem>Action</CDropdownItem>
                      <CDropdownItem>Another action</CDropdownItem>
                      <CDropdownItem>Something else here...</CDropdownItem>
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
  </>
  )
}

export default GardenMain
