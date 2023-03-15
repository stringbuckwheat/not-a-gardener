import {
  CCard,
  CCardBody,
  CCol,
  CRow
} from '@coreui/react'
import DeleteModal from '../../components/modal/DeleteModal';
import { EditOutlined } from '@ant-design/icons';
import { Space, Tooltip } from 'antd';

const DetailLayout = (props) => {
  const url = props.url;
  const path = props.path;
  const title = props.title;
  const deleteTitle = props.deleteTitle;
  const tags = props.tags;
  const onClickModifyBtn = props.onClickModifyBtn;
  const bottomData = props.bottomData;
  const deleteTooltipMsg = props.deleteTooltipMsg;

  return (
    <div className="row justify-content-md-center">
      <CCol md="auto">
        <CCard sm={6} className="mb-4" style={{ minWidth: '70%' }}>
          <CCardBody>
            <CRow>
              <CCol>
                <h4 className="mt-3 mb-3">{title}</h4>
              </CCol>
              <CCol>
                <div className="d-flex justify-content-end mt-3 mb-3">
                  <Space>
                      <EditOutlined onClick={onClickModifyBtn} style={{ fontSize: '18px', color: '#14A44D' }} />
                    <DeleteModal
                      url={url}
                      path={path}
                      title={deleteTitle}
                      deleteTooltipMsg={deleteTooltipMsg}
                    />
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
