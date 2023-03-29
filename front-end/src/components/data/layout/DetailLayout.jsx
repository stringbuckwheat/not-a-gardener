import {
  CCard,
  CCardBody,
  CCol,
  CRow
} from '@coreui/react'
import DeleteModal from '../../modal/DeleteModal';
import {EditOutlined} from '@ant-design/icons';
import {Space} from 'antd';

const DetailLayout = (props) => {
  const {url, path, title, deleteTitle, tags, onClickModifyBtn, bottomData, deleteTooltipMsg} = props;

  const deleteModal = props.deleteModal
    ? props.deleteModal
    : <DeleteModal
      url={url}
      path={path}
      title={deleteTitle}
      deleteTooltipMsg={deleteTooltipMsg}
    />

  return (
    <div className="row justify-content-md-center">
      <CCol md="auto" className="minWidth-70">
        <CCard sm={6} className="mb-4">
          <CCardBody>
            <CRow>
              <CCol>
                <h4 className="mt-3 mb-3">{title}</h4>
              </CCol>
              <CCol>
                <div className="d-flex justify-content-end mt-3 mb-3">
                  <Space>
                    <EditOutlined
                      className="font-size-18 text-success"
                      onClick={onClickModifyBtn}/>
                    {deleteModal}
                  </Space>
                </div>
              </CCol>
            </CRow>
            {tags}
            <div className="mt-3">
              {bottomData}
            </div>
          </CCardBody>
        </CCard>
      </CCol>
    </div>

  );
}

export default DetailLayout;
