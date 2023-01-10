import React, { useEffect, useState } from 'react'
import {
  CButton,
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CForm,
  CFormInput,
  CInputGroup,
  CInputGroupText,
  CRow,
  CFormSwitch,
  CFormCheck,
} from '@coreui/react'
import authAxios from '../requestInterceptor'
import { useNavigate, Navigate } from 'react-router-dom';

const ModifyPlant = (props) => {
  console.log("modify plant");
  const [ modifyPlant, setModifyPlant ] = useState({
    averageWateringPeriod: props.plant.averageWateringPeriod,
    plantName: props.plant.plantName,
    plantNo: props.plant.plantNo,
    plantSpecies: props.plant.plantSpecies
  })

  const [ initPeriod, setInitPeriod ] = useState(false);

  const onChange = (e) => {
    // 객체 세팅
    const {name, value} = e.target;
    setModifyPlant(setModifyPlant => ({...modifyPlant, [name]: value }))
    console.log("modifyPlant", modifyPlant);
  }

  const navigate = useNavigate();

  const onSubmit = (e) => {
    e.preventDefault();

    if(initPeriod){
      // TODO 물주기를 한 번 초기화하면 돌이킬 수 없다고 알려주기
      console.log("init period true");
      setModifyPlant(setModifyPlant => ({...modifyPlant, ["averageWateringPeriod"]: 5 }))
    }

    if(modifyPlant.plantName == ""){
      setModifyPlant(setModifyPlant => ({...modifyPlant, ["plantName"]: props.plant.plantName }))
    }

    if(modifyPlant.plantSpecies == ""){
      setModifyPlant(setModifyPlant => ({...modifyPlant, ["plantSpecies"]: props.plant.plantSpecies }))
    }

    authAxios.put("/garden/plant/" + modifyPlant.plantNo, modifyPlant)
    .then((res) => {
      console.log(res);
      navigate("/garden");
    })
  }

  return (
    <CForm className="mb-5" onSubmit={onSubmit}>
              <CInputGroup className="mb-3 mt-3">
                <CInputGroupText id="basic-addon1">이 식물의 이름은</CInputGroupText>
                <CFormInput
                  aria-describedby="basic-addon1"
                  name="plantName"
                  placeholder={props.plant.plantName}
                  onChange={onChange}
                />
                <CInputGroupText id="basic-addon1">이에요!</CInputGroupText>
              </CInputGroup>
              <CInputGroup className="mb-3">
                <CInputGroupText id="basic-addon1">식물 종은</CInputGroupText>
                <CFormInput
                  aria-describedby="basic-addon1"
                  name="plantSpecies"
                  placeholder={props.plant.plantSpecies}
                  onChange={onChange}
                />
                <CInputGroupText id="basic-addon1">이구요</CInputGroupText>
              </CInputGroup>
              <CInputGroup className="mb-3">
                <CInputGroupText id="basic-addon2">최근 물주기는</CInputGroupText>
                <CFormInput
                  name="averageWateringPeriod"
                  disabled={true}
                  placeholder={props.plant.averageWateringPeriod}
                  onChange={onChange}
                />
                <CInputGroupText id="basic-addon2">일 입니다</CInputGroupText>
              </CInputGroup>
                  <CFormCheck id="init-period" label="물주기 정보를 초기화하시겠습니까?" onClick={() => setInitPeriod(!initPeriod)}/>
                  <div>{initPeriod ? "물주기 정보가 5일로 초기화됩니다." : ""}</div>
                  <div className="d-flex justify-content-end">
                    <CButton type="submit" color='success'>수정</CButton>
                  </div>
            </CForm> 
  )
}

export default ModifyPlant
