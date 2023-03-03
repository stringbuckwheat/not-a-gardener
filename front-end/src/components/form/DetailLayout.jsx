import {
    CCard,
    CCardBody,
    CCardHeader,
    CCol,
    CContainer,
  } from '@coreui/react'
import { Space, Tag } from 'antd';

const DetailLayout = (props) => {
  const title = props.title;
  const bottomData = props.bottomData;
  const tags = props.tags;

    return(
      <CContainer>
        <div className="row justify-content-md-center">
          <CCol md="auto">
            <CCard sm={6} className="mb-4">
              <CCardBody>
                <div className="mb-4">
                  <h4 className="mt-3 mb-3">{title}</h4>
                  {tags}
                </div>
                {bottomData}
              </CCardBody>
            </CCard>
          </CCol>
        </div>
      </CContainer>
    );
}

export default DetailLayout;
