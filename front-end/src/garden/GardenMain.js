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
import { getStyle } from '@coreui/utils'
// import { CChartBar, CChartLine } from '@coreui/react-chartjs'
import CIcon from '@coreui/icons-react'
import { cilArrowBottom, cilArrowTop, cilOptions } from '@coreui/icons'
import axios from 'axios'

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
     axios.get("/garden", "")
        .then((res) => {
          console.log("res.data");
          console.log(res.data);
          setPlantList(res.data);
          })
        .catch(error => console.log(error))
  }, [])

  return (
  <>
      <CRow>
       {plantList.map((plant, idx) => {
          console.log(plant);

          const color = ["primary", "warning", "danger", "success"];
          let message = "";

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
          }


         return (
            <CCol sm={6} lg={3}>
              <CWidgetStatsA
                className="mb-4"
                color={color[plant.wateringCode]}
                value={
                  <>
                    <span role="img" aria-label="herb">🌿 </span>
                        {plant.plantName}{' '}
                    <span role="img" aria-label="herb">🌿</span>

                    <div className="fs-6 fw-normal">
                      <div>{plant.plantSpecies}</div>
                      (이 식물의 평균 물주기는 {plant.averageWateringPeriod}일입니다.)
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

      //          chart={
      //            <CChartLine
      //              className="mt-3 mx-3"
      //              style={{ height: '70px' }}
      //              data={{
      //                labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
      //                datasets: [
      //                  {
      //                    label: 'My First dataset',
      //                    backgroundColor: 'transparent',
      //                    borderColor: 'rgba(255,255,255,.55)',
      //                    pointBackgroundColor: getStyle('--cui-primary'),
      //                    data: [65, 59, 84, 84, 51, 55, 40],
      //                  },
      //                ],
      //              }}
      //              options={{
      //                plugins: {
      //                  legend: {
      //                    display: false,
      //                  },
      //                },
      //                maintainAspectRatio: false,
      //                scales: {
      //                  x: {
      //                    grid: {
      //                      display: false,
      //                      drawBorder: false,
      //                    },
      //                    ticks: {
      //                      display: false,
      //                    },
      //                  },
      //                  y: {
      //                    min: 30,
      //                    max: 89,
      //                    display: false,
      //                    grid: {
      //                      display: false,
      //                    },
      //                    ticks: {
      //                      display: false,
      //                    },
      //                  },
      //                },
      //                elements: {
      //                  line: {
      //                    borderWidth: 1,
      //                    tension: 0.4,
      //                  },
      //                  point: {
      //                    radius: 4,
      //                    hitRadius: 10,
      //                    hoverRadius: 4,
      //                  },
      //                },
      //              }}
      //            />
      //          }
              />
            </CCol>
          )
      })}
    </CRow>
  </>
  )
}

export default GardenMain
