import {
  CCardGroup,
  CCol,
  CContainer,
  CRow,
} from '@coreui/react'
import {ReactComponent as Logo} from "../../assets/images/logo.svg";

const LoginPageWrapper = ({children}) => {

  return (
    <div className="min-vh-100 bg-garden">
      <div className="min-vh-100 d-flex align-items-center">
        <CContainer className="row min-vw-100">
          <div>
            <Logo className="float-end" width={"60vw"} height={"15vh"} fill={"#E14A1E"}/>
          </div>
          <CRow className="d-flex align-items-center justify-content-center width-full">
            <CCol md={8}>
              <CCardGroup>
                {children}
              </CCardGroup>
            </CCol>
          </CRow>
        </CContainer>
      </div>
    </div>
  )

}

export default LoginPageWrapper
