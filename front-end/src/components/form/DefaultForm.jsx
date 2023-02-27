import {
    CButton,
    CCard,
    CCardBody,
    CCardHeader,
    CCol,
    CContainer,
    CForm,
  } from '@coreui/react'
  import { useState } from 'react'
  import FormInputText from './input/FormInputText'
  import FormInputSelect from './input/FormInputSelect'
  import DeleteModal from 'src/components/modal/DeleteModal'
  import FormInputSwitch from './input/FormInputSwitch'
  import FormSubmitButton from '../button/FormSubmitButton'

const DefaultForm = (props) => {
    const itemObjectArray = props.itemObjectArray;
    const [inputObject, setInputObject] = useState(props.inputObject);
    const requiredValueArray = [];
    const [validation, setValidation] = useState(false);

    const onChange = (e) => {
      const { name, value } = e.target;

      // 빈칸 검사
      if(requiredValueArray.includes(name)){
        setValidation(value !== "");
      }

      setInputObject(setInputObject => ({...inputObject, [name]: value}));
    }

    // 빈칸 검사 후 메시지
    const invalidMsg = () => {
      if(inputObject.placeName == ""){
        return "장소 이름은 비워둘 수 없어요";
      }

      return "";
    }

    // 스위치 클릭 값 바꾸기
    const switchClick = () => {
      setInputObject(setInputObject => ({...inputObject, artificialLight: !inputObject.artificialLight}));
    }

    const getLabelForSwitch = (name) => {
      console.log("스위치 라벨", name);
      
      if(name === 'artificialLight'){
        return inputObject.artificialLight ? "식물등을 사용합니다" : "식물등을 사용하지 않습니다";
      }   
    }

    // 삭제 모달 용 변수, 함수
    const [visible, setVisible] = useState(false);
    const closeDeleteModal = () => {
        setVisible(false);
    }

    return(
      <CContainer>
        <div className="row justify-content-md-center">
          <CCol md="auto">
            <CCard sm={6} className="mb-4">
              <CCardHeader>
                <h4 className="mt-3">{props.title + " " + props.action}</h4>
              </CCardHeader>
              <CCardBody>
                <CForm validated={true}>
                  {/* input 받을 내용 */

                  itemObjectArray.map((inputItem) => {

                    // 빈칸 검사의 대상이면 배열에 넣음
                    if(inputItem.required){
                      requiredValueArray.push(inputItem.name);
                    }

                    if(inputItem.inputType === "text"){
                      return(
                        <FormInputText 
                          inputItem={inputItem} 
                          onChange={onChange} 
                          feedbackInvalid={invalidMsg()}
                          />
                      )
                    } else if(inputItem.inputType === "select"){
                      return(
                        <FormInputSelect 
                          inputItem={inputItem} 
                          onChange={onChange} />
                      )
                    } else if(inputItem.inputType === "switch"){

                      return(
                        <FormInputSwitch 
                          inputItem={inputItem} 
                          onClick={switchClick}
                          value={inputObject[inputItem.name]}
                          label={getLabelForSwitch(inputItem.name)}/>
                      )
                    }
                  })
                }
                  <div className="d-flex justify-content-end">
                    {!props.isNew
                      ? 
                      <>
                        <DeleteModal 
                          url={props.submitUrl}
                          path={props.path} 
                          title={props.title}
                          visible={visible} 
                          closeDeleteModal={closeDeleteModal} 
                          />
                        <CButton color="link-secondary" onClick={() => setVisible(true)}><small>삭제</small></CButton>
                      </>
                    : <></>}
                    <FormSubmitButton 
                      url={props.submitUrl} 
                      path={props.path}
                      data={inputObject} 
                      buttonName={props.action} 
                      validation={validation} />
                  </div>
                  </CForm>
              </CCardBody>
            </CCard>
          </CCol>
        </div>
      </CContainer>
    );
}

export default DefaultForm;