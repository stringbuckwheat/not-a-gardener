import {CCard, CCardBody, CCardGroup, CCol, CContainer, CForm, CRow} from '@coreui/react'
import Routine from "./routine/Routine";

const Schedule = () => {

  return (
    <CContainer>
      <CCardGroup>
        <Routine/>
        <CCard className="p-4">
          <CCardBody>
            <div>목표</div>
          </CCardBody>
        </CCard>
      </CCardGroup>

    </CContainer>
  )
}

export default Schedule;
