import {
    CCard,
    CCardBody,
    CCol,
    CContainer,
    CButton
} from '@coreui/react'
import { useState } from 'react';
import AddPlantButton from 'src/components/button/AddPlantButton';

const PlantListLayout = (props) => {

    const url = props.url;
    const path = props.path;
    const title = props.title;
    const deleteTitle = props.deleteTitle;
    const tags = props.tags;
    const onClickModifyBtn = props.onClickModifyBtn;
    const bottomData = props.bottomData;

    // 삭제 모달 용 변수, 함수 -- addForm과 다른 점...
    const [visible, setVisible] = useState(false);
    const closeDeleteModal = () => {
        setVisible(false);
    }

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
