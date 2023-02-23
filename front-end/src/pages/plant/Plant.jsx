import React, { useEffect, useState } from 'react'
import {
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CRow,
  CFormSwitch,
} from '@coreui/react'
import authAxios from '../../utils/requestInterceptor'
import PlantLog from './PlantLog'
import ModifyPlant from './ModifyPlant'
import { useParams } from 'react-router-dom'

const Plant = (props) => {
  // useParam: URL 파라미터를 받아온다.
  // :plantNo 꼴로 Route에 정의해놓으면, 객체 모양으로 받아옴
  const paramPlantNo = useParams().plantNo;
  
  // 스위치가 active 상태가 되면, input창 disabled 풀리고 물주기 초기화 체크박스와 수정 버튼 생김
  const [ modifySwitch, setModifySwitch ] = useState(false);
  const [ plant, setPlant ] = useState({
    averageWateringPeriod: 0,
    fertilizingCode: -1,
    plantName: '',
    plantNo: -1,
    plantSpecies: ''
  })
  const [ waterList, setWaterList ] = useState([{
      plantNo: -1,
      fertilized: '',
      wateringDate: ''
  }])

  const [ logArrival, setLogArrival ] = useState(false);

  // 이 식물의 detail 정보 받아오기
  // plant 테이블의 1 row와 물주기 정보들
  // 왜 axios는 useEffect랑 같이 쓰지?
  useEffect(() => {
    authAxios.get("/garden/plant/" + paramPlantNo, "")
      .then((res) => {
        console.log("res", res);
        setPlant(res.data);
        setWaterList(res.data.waterDtoList);
        setLogArrival(true);
      })
      .catch(error => console.log(error))
  }, [])

  const modifySwitchOff = () => {
    setModifySwitch(false);
  }

  
  return (
    <CRow>
      <CCol xs={12}>
        <CCard className="mb-4">
          <CCardHeader>
            <div>
            <strong>{plant.plantName}</strong> <small>🌱</small>
            </div>
            <div>
              {plant.plantSpecies}, 최근 물주기: {plant.averageWateringPeriod}
            </div>
            <div className="d-flex justify-content-end">
              <CFormSwitch className="text-medium-emphasis small" label="수정하기" id="formSwitchCheckDefault" onClick={() => setModifySwitch(!modifySwitch)}/>
            </div>    
          </CCardHeader>
          <CCardBody>
            {modifySwitch
              ? <ModifyPlant plant={plant} modifySwitchOff={modifySwitchOff}/>
              : <></>}
            { logArrival && !modifySwitch
              ? <PlantLog waterList={waterList}/>
              : <></>}
          </CCardBody>
        </CCard>
      </CCol>
    </CRow>
  )
}

export default Plant
