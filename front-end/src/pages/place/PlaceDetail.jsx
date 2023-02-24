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
  } from '@coreui/react'
  import { useState } from 'react'
  import { useNavigate } from 'react-router-dom'
  import authAxios from '../../utils/requestInterceptor'
  import { useLocation } from 'react-router-dom'
  import DeleteModal from 'src/components/modal/DeleteModal'

const PlaceDetail = () => {
    console.log("place detail");
    
    // props
    const { state } = useLocation();
    console.log("state", state);

    // 제목, 버튼 이름 구하기
    let title = "";
    let buttonName = "";

    if(state.placeNo === undefined){
      title = "장소 추가";
      buttonName = "추가하기"
    } else {
      title = "장소 수정";
      buttonName = "수정하기";
    }
    
    // submit용 객체
    const [place, setPlace] = useState({
      placeNo: state.placeNo,
      placeName: state.placeName,
      option: state.option,
      artificialLight: state.artificialLight
    });

    // input 유효성 검사
    const [validation, setValidation] = useState(true);
    
    // input 변화 감지
    const onChange = (e) => {
        const { name, value } = e.target;
        setPlace(setPlace => ({...place, [name]: value}));

        if(name === "placeName"){
          setValidation(place.placeName !== "" ? true : false);
        }

        console.log("place", place);
    }

    // 식물등 스위치 제어
    const swichClick = () => {
        setPlace(setPlace => ({...place, artificialLight: !place.artificialLight}));
    }

    // 페이지 전환
    const navigate = useNavigate();

    const onSubmit = () => {
        console.log("onModify place", place);

        // 등록
        if(state.placeNo === undefined){
          console.log("post 요청")

          authAxios.post("/place", place)
          .then((res) => {
            navigate("/place");
          })
        } else {
          // 수정
          console.log("put 요청")

          authAxios.put("/place", place)
          .then((res) => {
            navigate("/place/" + place.placeNo);
          })
        }
    }

    // 삭제 모달 용 변수, 함수
    const [visible, setVisible] = useState(false);
    const closeDeleteModal = () => {
        setVisible(false);
    }
    const deleteCallBackFunction = () => {
      navigate("/place");
    }

    return(
      <CContainer fluid>
      <div className="row justify-content-md-center">
      <CCol sm={6}>
          <CCard sm={6} className="mb-4">
            <CCardHeader>
              <h4 className="mt-3">{title}</h4>
            </CCardHeader>
            <CCardBody>
              <CForm validated={true}>
                <CInputGroup className="mb-3 mt-3">
                  <CInputGroupText id="basic-addon1">장소 이름</CInputGroupText>
                  <CFormInput
                    defaultValue={state.placeName}
                    aria-describedby="basic-addon1"
                    name="placeName"
                    required
                    onChange={onChange}
                    feedbackInvalid={state.placeName === "" ? "장소 이름은 비워둘 수 없습니다" : ""}
                  />
                </CInputGroup>
                <CInputGroup className="mb-3">
                  <CInputGroupText id="basic-addon1">이 장소의 위치</CInputGroupText>
                    <CFormSelect name="option" onChange={onChange} defaultValue={state.option}>
                        <option value="실내">실내</option>
                        <option value="베란다">베란다</option>
                        <option value="야외">야외</option>
                    </CFormSelect>
                </CInputGroup>
                <CFormSwitch 
                    className="text-medium-emphasis" 
                    name="artificialLight" 
                    onClick={swichClick} 
                    size="lg" 
                    label={state.artificialLight === false 
                        ? "식물등을 사용하지 않습니다" 
                        : "식물등을 사용합니다"} 
                    id="formSwitchCheckChecked"/>
                <div className="d-flex justify-content-end mt-3">
                  {validation
                  ? <CButton type="button" color='success' onClick={onSubmit}>{buttonName}</CButton>
                  : <CButton type="button" color='secondary' variant="outline" disabled>{buttonName}</CButton>}
                  {state.placeNo !== undefined
                  ? 
                    <>
                      <DeleteModal 
                        visible={visible} 
                        deleteItem={state.placeNo} 
                        closeDeleteModal={closeDeleteModal} 
                        title={"장소"}
                        deleteUrl={"/place/" + state.placeNo} 
                        callBackFunction={deleteCallBackFunction}/>
                      <CButton color="link-secondary" onClick={() => setVisible(true)}><small>삭제하기</small></CButton>
                    </>
                  : <></>}
                </div>
                </CForm>
            </CCardBody>
          </CCard>
          </CCol>
          </div>
      </CContainer>
    );
}

export default PlaceDetail;