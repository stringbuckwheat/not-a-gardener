import {CCard, CCardBody} from "@coreui/react";
const ForgotCardWrapper = ({children}) => {
  return (
    <CCard
      style={{minHeight: "50vh"}}
      className={`bg-beige`}>
      <CCardBody className="d-flex align-items-center justify-content-center">
        {children}
      </CCardBody>
    </CCard>
  )
}

export default ForgotCardWrapper;
