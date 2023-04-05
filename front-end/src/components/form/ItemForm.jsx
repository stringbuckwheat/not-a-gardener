import {CCard, CCardBody, CCol, CForm} from '@coreui/react'
import FormInputHandler from './FormInputHandler'

const ItemForm = ({title, itemObjectArray, submitBtn, inputObject, onChange}) => {
  return (
    <div className="row justify-content-md-center align-items-center height-95">
      <CCol md="auto" className="minWidth-70">
        <CCard sm={6} className="mb-4">
          <CCardBody>
            <h4 className="mt-3 mb-3">{title}</h4>
            <CForm validated={true}>
              {/* input, select 등을 구해서 채움 */}
              <FormInputHandler
                itemObjectArray={itemObjectArray}
                onChange={onChange}
                inputObject={inputObject}/>
              {/* 등록 제출 버튼 */}
              {submitBtn}
            </CForm>
          </CCardBody>
        </CCard>
      </CCol>
    </div>
  );
}

export default ItemForm;
