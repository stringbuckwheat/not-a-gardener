import {CButton, CCard, CCardBody, CCol} from '@coreui/react'
import Search from "../data/header/search/Search";
import {Space} from "antd";

const PlantListLayout = ({title, tags, bottomData, setSearchWord, addFormOpen}) => {
  return (
    <div className="row justify-content-md-center">
      <CCol md="auto" className="minWidth-full">
        <CCard sm={6} className="mb-4">
          <CCardBody>
            <div>
              <h4 className="mt-3 mb-3">{title}</h4>
              {tags}
            </div>
            <div className="float-end mb-3">
              <Space>
                <Search setSearchWord={setSearchWord}/>
                <CButton
                  onClick={addFormOpen}
                  color="success"
                  size="sm"
                  variant="outline"
                  shape="rounded-pill">
                  식물 추가하기
                </CButton>
              </Space>
            </div>
            {bottomData}
          </CCardBody>
        </CCard>
      </CCol>
    </div>
  );
}

export default PlantListLayout;
