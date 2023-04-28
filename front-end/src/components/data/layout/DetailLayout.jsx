import {
  CCard,
  CCardBody,
  CCol,
  CRow
} from '@coreui/react'
import DeleteModal from '../../modal/DeleteModal';
import {EditOutlined} from '@ant-design/icons';
import {Space} from 'antd';

/**
 * 상세 페이지 레이아웃
 * @param url
 * @param path
 * @param title
 * @param deleteTitle
 * @param tags
 * @param onClickModifyBtn
 * @param bottomData
 * @param deleteTooltipMsg
 * @param deleteCallBackFunction
 * @param deleteModal
 * @returns {JSX.Element}
 * @constructor
 */
const DetailLayout = ({
                        url,
                        path,
                        title,
                        deleteTitle,
                        tags,
                        onClickModifyBtn,
                        bottomData,
                        deleteTooltipMsg,
                        deleteCallBackFunction,
                        deleteModal
                      }) => {

  // default 삭제 모달
  deleteModal ??= <DeleteModal
    url={url}
    path={path}
    title={deleteTitle}
    deleteCallBackFunction={deleteCallBackFunction}
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
