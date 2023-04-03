import {
  CCard,
  CCardBody,
  CCol,
  CContainer,
  CRow,
} from '@coreui/react'
import {ReactComponent as Logo} from "../../../assets/images/logo.svg";

const MemberFormWrapper = ({children}) => {

  return (
    <div className="bg-garden min-vh-100 d-flex flex-row align-items-center">
      <CContainer>
        <div>
          <Logo className="float-end" width={"60vw"} height={"15vh"} fill={"#E14A1E"}/>
        </div>
        <CRow className="d-flex align-items-center justify-content-center width-full">
          <CCol md={8}>
            <CCard className="mx-4">
              <CCardBody className="p-4">
                {children}
              </CCardBody>
            </CCard>
          </CCol>
        </CRow>
      </CContainer>
    </div>
  )
}

export default MemberFormWrapper;
