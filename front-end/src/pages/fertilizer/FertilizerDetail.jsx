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
  } from '@coreui/react'
  import { useState } from 'react'
  import { useNavigate } from 'react-router-dom'
  import authAxios from '../../utils/interceptors'
  import { useLocation } from 'react-router-dom'
  import DeleteModal from 'src/components/modal/DeleteModal'

const FertilizerDetail = () => {
    
    // props
    const { state } = useLocation();

    console.log("state", state);

    // 제목, 버튼 이름 구하기
    let title = "";
    let buttonName = "";

    if(state.fertilizerNo === undefined){
      title = "비료 추가";
      buttonName = "추가하기"
    } else {
      title = "비료 수정";
      buttonName = "수정하기";
    }
    
    // submit용 객체에 props로 넘긴 값 복사
    const [fertilizer, setFertilizer] = useState({
        fertilizerNo: state.fertiliezerNo,
        fertilizerName: state.fertilizerName,
        fertilizerType: state.fertilizerType,
        fertilizingPeriod: state.fertilizingPeriod
    });

    // input 유효성 검사
    const [validation, setValidation] = useState(fertilizer.fertilizerName !== "" 
                                                && Number.isInteger(fertilizer.fertilizingPeriod * 1)
                                                && fertilizer.fertilizingPeriod * 1 > 0);
    
    // input 변화 감지
    const onChange = (e) => {
        const { name, value } = e.target;
        setFertilizer(setFertilizer => ({...fertilizer, [name]: value}));

        setValidation(fertilizer.fertilizerName !== "" 
                    && Number.isInteger(fertilizer.fertilizingPeriod * 1)
                    && fertilizer.fertilizingPeriod * 1 > 0);
        
        console.log("validation", validation);
        console.log("fertilizer.fertilizerName !== ", fertilizer.fertilizerName !== "");
        console.log("Number.isInteger(fertilizer.fertilizingPeriod * 1)", Number.isInteger(fertilizer.fertilizingPeriod * 1));

        console.log("onChange ==> fertilizer", fertilizer);
    }

    // 페이지 전환
    const navigate = useNavigate();

    const onSubmit = () => {
        console.log("fertilizer", fertilizer);
        // 등록
        if(state.fertilizerNo === undefined){
          console.log("post 요청");
          
          authAxios.post("/fertilizer", fertilizer)
          .then((res) => {
            navigate("/fertilizer");
          })
        } else {
          // 수정
          console.log("put 요청")

          authAxios.put("/fertilizer", fertilizer)
          .then((res) => {
            navigate("/fertilizer/" + fertilizer.fertilizerNo);
          })
        }
    }

    // 삭제 모달 용 변수, 함수
    const [visible, setVisible] = useState(false);
    const closeDeleteModal = () => {
        setVisible(false);
    }
    const deleteCallBackFunction = () => {
      navigate("/fertilizer");
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
                <CInputGroup className="mb-3 mt-3 has-validation">
                  <CInputGroupText id="inputGroupPrepend03">비료 이름</CInputGroupText>
                  <CFormInput
                    type="text"
                    defaultValue={fertilizer.fertilizerName}
                    aria-describedby="basic-addon1"
                    name="fertilizerName"
                    required
                    onChange={onChange}
                    feedbackInvalid={state.fertilizerName === "" ? "비료 이름은 비워둘 수 없습니다" : ""}
                  />
                </CInputGroup>

                <CInputGroup className="mb-3">
                  <CInputGroupText id="basic-addon1">비료 종류</CInputGroupText>
                    <CFormSelect name="fertilizerType" onChange={onChange} defaultValue={fertilizer.fertilizerType}>
                        <option value="기본 NPK 비료">기본 NPK 비료</option>
                        <option value="개화용 비료">개화용 비료</option>
                        <option value="미량 원소 비료">미량 원소 비료</option>
                        <option value="그 외">그 외</option>
                    </CFormSelect>
                </CInputGroup>

                <CInputGroup className="mb-3 mt-3">
                  <CInputGroupText id="basic-addon1">비료 주기는</CInputGroupText>
                  <CFormInput
                    defaultValue={fertilizer.fertilizingPeriod}
                    type="number"
                    aria-describedby="basic-addon1"
                    name="fertilizingPeriod"
                    required
                    onChange={onChange}
                    feedbackInvalid={Number.isInteger(fertilizer.fertilizingPeriod) ? "" : "숫자를 입력해주세요"}
                  />
                  <CInputGroupText id="basic-addon1">일에 한 번</CInputGroupText>
                </CInputGroup>
                
                <div className="d-flex justify-content-end mt-3">
                  {validation
                  ? <CButton type="button" color='success' onClick={onSubmit}>{buttonName}</CButton>
                  : <CButton type="button" color='secondary' variant="outline" disabled>{buttonName}</CButton>}
                  {state.fertilizerNo !== undefined
                  ? 
                    <>
                      <DeleteModal 
                        visible={visible} 
                        deleteItem={state.fertilizerNo} 
                        closeDeleteModal={closeDeleteModal} 
                        title={"비료"}
                        deleteUrl={"/fertilizer/" + state.fertilizerNo} 
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

export default FertilizerDetail;