import {
  CCard,
  CCardBody,
  CCol,
  CRow
} from '@coreui/react'
import DeleteModal from '../../modal/DeleteModal';
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
  const style = { fontSize: '18px', color: '#14A44D' }
  const minWidth = { minWidth: '70%' }

  return (
    <div className="row justify-content-md-center">
      <CCol md="auto"  style={minWidth}>
        <CCard sm={6} className="mb-4">
          <CCardBody>
            <CRow>
              <CCol>
                <h4 className="mt-3 mb-3">{title}</h4>
              </CCol>
              <CCol>
                <div className="d-flex justify-content-end mt-3 mb-3">
                  <Space>
                      <EditOutlined onClick={onClickModifyBtn} style={style} />
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
