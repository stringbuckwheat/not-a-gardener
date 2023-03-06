import {
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CContainer,
  CForm,
} from '@coreui/react'
import { useState } from 'react'
import FormInputHandler from './FormInputHandler'
import SubmitForAddButton from '../button/SubmitForAddButton'

const AddForm = (props) => {
  //// props
  // title: 식물, 비료, 살충제...
  const title = props.title;

  // 제출용 객체
  const [inputObject, setInputObject] = useState(props.inputObject);

  // input 만들 정보가 담긴 배열
  const itemObjectArray = props.itemObjectArray;

  // 빈칸 검사를 해야 할 input 목록
  const requiredValueArray = props.requiredValueArray;

  // 숫자인지 검사해야 할 input 목록
  const isNumberArray = props.isNumberArray;

  // 제출할 url
  const url = props.submitUrl;

  // form이 유효성 검사를 완료했는지
  const [validation, setValidation] = useState(false);

  // input 값 감지하여 제출용 객체 채우기
  const onChange = (e) => {
    const { name, value } = e.target;

    // 유효성 검사
    if (requiredValueArray.includes(name)) {
      setValidation(value !== "");
    } else if (isNumberArray.includes(name)) {
      setValidation(Number.isInteger(value * 1));
    } else {
      setValidation(true);
    }

    setInputObject(setInputObject => ({ ...inputObject, [name]: value }));
    console.log("=== onchange", inputObject);
  }

  return (
    <CContainer>
      <div className="row justify-content-md-center">
        <CCol md="auto">
          <CCard sm={6} className="mb-4">
            <CCardHeader>
              <h4 className="mt-3">{title} 추가</h4>
            </CCardHeader>
            <CCardBody>
              <CForm validated={true}>
                {/* input, select 등을 구해서 채움 */}
                <FormInputHandler
                  itemObjectArray={itemObjectArray}
                  onChange={onChange}
                  inputObject={inputObject} />

                {/* 등록 제출 버튼 */}
                <div className="d-flex justify-content-end">
                  <SubmitForAddButton
                    url={url}
                    data={inputObject}
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

export default AddForm;
