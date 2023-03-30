import {
  CCard,
  CCardBody,
  CCol,
  CContainer,
  CRow,
} from '@coreui/react'
const MemberFormWrapper = ({children}) => {

  return (
    <CContainer>
      <CRow className="justify-content-center">
        <CCol md={8}>
          <CCard className="mx-4">
            <CCardBody className="p-4">
              {children}
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
    </CContainer>
  )
}

export default MemberFormWrapper;
