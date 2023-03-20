import {
    CCard,
    CCardBody,
    CCol,
    CContainer,
} from '@coreui/react'
import AddPlantButton from 'src/components/button/AddPlantButton';

const PlantListLayout = (props) => {
    const title = props.title;
    const tags = props.tags;
    const bottomData = props.bottomData;

    return (
        <CContainer>
            <div className="row justify-content-md-center">
                <CCol md="auto">
                    <CCard sm={6} className="mb-4">
                        <CCardBody>
                            <div>
                                <h4 className="mt-3 mb-3">{title}</h4>
                                {tags}
                            </div>
                            <div className="d-flex justify-content-end mb-3">
                                <AddPlantButton size="sm" />
                            </div>
                            {bottomData}
                        </CCardBody>
                    </CCard>
                </CCol>
            </div>
        </CContainer>
    );
}

export default PlantListLayout;
