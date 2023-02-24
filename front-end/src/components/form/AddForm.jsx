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
    CInputGroupText,
    CFormSelect,
    CFormSwitch,
    CFormFeedback
  } from '@coreui/react'
  import { useState } from 'react'
  import { useNavigate } from 'react-router-dom'
  import authAxios from '../../utils/requestInterceptor'

const AddForm = () => {
    const [place, setPlace] = useState({
        placeName: "",
        option: "실내",
        artificialLight: false
    })

    const onChange = (e) => {
        const { name, value } = e.target;
        setPlace(setPlace => ({...place, [name]: value}));

        if(name === "placeName"){
          setValidation(place.placeName !== "" ? true : false);
        }

        console.log("place", place);
    }

    const swichClick = () => {
        setPlace(setPlace => ({...place, artificialLight: !place.artificialLight}));
    }

    const onSubmit = () => {
        console.log("onSubmit place", place);
    }

    const [validation, setValidation] = useState(false);

    return(
      <CContainer>
      <div className="row justify-content-md-center">
      <CCol md="auto">
          <CCard sm={6} className="mb-4">
            <CCardHeader>
              <h4 className="mt-3">새 장소 추가하기</h4>
            </CCardHeader>
            <CCardBody>
              <CForm validated={true}>
                <CInputGroup className="mb-3 mt-3">
                  <CInputGroupText id="basic-addon1">장소 이름</CInputGroupText>
                  <CFormInput
                    value={place.placeName}
                    aria-describedby="basic-addon1"
                    name="placeName"
                    required
                    onChange={onChange}
                    feedbackInvalid={place.placeName === "" ? "장소 이름은 비워둘 수 없습니다" : ""}
                  />
                </CInputGroup>
                <CInputGroup className="mb-3">
                  <CInputGroupText id="basic-addon1">이 장소는</CInputGroupText>
                    <CFormSelect name="option" onChange={onChange}>
                        <option value="실내">실내</option>
                        <option value="베란다">베란다</option>
                        <option value="야외">야외</option>
                    </CFormSelect>
                  <CInputGroupText id="basic-addon1">고요</CInputGroupText>
                </CInputGroup>
                <CFormSwitch 
                    className="text-medium-emphasis" 
                    name="artificialLight" 
                    onClick={swichClick} 
                    size="lg" 
                    label={place.artificialLight === false 
                        ? "식물등을 사용하지 않습니다" 
                        : "식물등을 사용합니다"} 
                    id="formSwitchCheckChecked"/>
                <div className="d-flex justify-content-end">
                  {validation
                  ? <CButton type="button" color='success' onClick={onSubmit}>추가</CButton>
                  : <CButton type="button" color='secondary' variant="outline" disabled>추가</CButton>}
                </div>
                </CForm>
            </CCardBody>
          </CCard>
          </CCol>
          </div>
      </CContainer>
    );
}

export default AddForm;