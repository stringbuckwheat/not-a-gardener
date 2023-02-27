import {
  CButton,
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CContainer,
  CForm,
  CFormInput,
  CInputGroup,
  CInputGroupText
} from '@coreui/react'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import authAxios from '../../utils/requestInterceptor'

const PlantDetail = () => {
  // submit용 객체
  const [plant, setPlant] = useState({
    plantName: "",
    plantSpecies: "",
    averageWateringPeriod: 5
  })

  const navigate = useNavigate();

  // 입력창 변경 시 setPlant
  const onChange = (e) => {
    const { name, value } = e.target;
    setPlant(setPlant => ({...plant, [name]: value}));
    console.log(plant)
  }

  const onSubmit = (e) => {
    e.preventDefault();

    console.log("onSubmit clicked");
    console.log("plant", plant);

    if(plant.plantName == ""){
      alert("식물 이름은 비워둘 수 없습니다.");
      return;
    }

    if(plant.averageWateringPeriod <= 0){
      setPlant(setPlant => ({...plant, averageWateringPeriod: 5}));
    }

    authAxios.post("/garden/plant", plant)
    .then((res) => {
      console.log("res", res);
      if(res.status === 200){
        navigate('/garden');
      }
    });
  }

  return (
    <CContainer>
      <div className="row justify-content-md-center">
      <CCol md="auto">
          <CCard sm={6} className="mb-4">
            <CCardHeader>
              <h5 className="mt-3">새 식물 추가하기 </h5>
            </CCardHeader>
            <CCardBody>
              <CForm onSubmit={onSubmit}>
                <CInputGroup className="mb-3 mt-3">
                  <CInputGroupText id="basic-addon1">이 식물의 이름은</CInputGroupText>
                  <CFormInput
                    aria-describedby="basic-addon1"
                    name="plantName"
                    required
                    onChange={onChange}
                  />
                  <CInputGroupText id="basic-addon1">이에요!</CInputGroupText>
                </CInputGroup>
                <CInputGroup className="mb-3">
                  <CInputGroupText id="basic-addon1">식물 종은</CInputGroupText>
                  <CFormInput
                    aria-describedby="basic-addon1"
                    name="plantSpecies"
                    onChange={onChange}
                  />
                  <CInputGroupText id="basic-addon1">이구요</CInputGroupText>
                </CInputGroup>
                <CInputGroup className="mb-3">
                  <CInputGroupText id="basic-addon2">평균 물주기는</CInputGroupText>
                  <CFormInput
                    name="averageWateringPeriod"
                    onChange={onChange}
                  />
                  <CInputGroupText id="basic-addon2">일 입니다</CInputGroupText>
                </CInputGroup>
                <p className="text-medium-emphasis small mt-1">
                      * 새 식물이거나 평균 물주기를 잘 모르겠다면 비워둬도 좋아요
                </p>
                <div className="d-flex justify-content-end">
                    <CButton type="submit" color='success'>추가</CButton>
                </div>
              </CForm>
            </CCardBody>
          </CCard>
          </CCol>
          </div>
      </CContainer>
  )
}

export default PlantDetail
