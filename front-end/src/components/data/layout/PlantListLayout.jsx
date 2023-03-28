import {
  CCard,
  CCardBody,
  CCol,
} from '@coreui/react'
import AddPlantButton from 'src/components/button/AddPlantButton';
import Search from "../header/search/Search";
import {Space} from "antd";

const PlantListLayout = (props) => {
  const {title, tags, bottomData, setSearchWord} = props;

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
                <AddPlantButton size="sm"/>
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
