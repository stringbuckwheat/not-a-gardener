import {
  CCard,
  CCardBody,
  CCol,
  CContainer,
  CButton
} from '@coreui/react'
import DeleteModal from '../modal/DeleteModal';
import { useState } from 'react';

const DetailLayout = (props) => {
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
      <div className="row justify-content-md-center">
        <CCol md="auto" style={{minWidth: '70%'}}>
          <CCard sm={6} className="mb-4">
            <CCardBody>
              <div>
                <h4 className="mt-3 mb-3">{title}</h4>
                {tags}
              </div>
              <div className="d-flex justify-content-end mt-3 mb-3">
                <DeleteModal
                  url={url}
                  path={path}
                  title={deleteTitle}
                  visible={visible}
                  closeDeleteModal={closeDeleteModal}
                />
                <CButton onClick={() => {setVisible(true)}}color="dark" variant="ghost" size="sm">삭제</CButton>
                <CButton onClick={onClickModifyBtn} type="button" color="success" variant="outline" size="sm">수정</CButton>
              </div>
              {bottomData}
            </CCardBody>
          </CCard>
        </CCol>
      </div>

  );
}

export default DetailLayout;
