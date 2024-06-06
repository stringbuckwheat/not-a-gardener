import DeleteModal from '../../modal/DeleteModal';
import {EditOutlined} from '@ant-design/icons';
import {Card, Col, Row, Space} from 'antd';

/**
 * 상세 페이지 레이아웃
 * @param url
 * @param path
 * @param title
 * @param deleteTitle
 * @param tags
 * @param onClickModifyBtn
 * @param children
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
                        children,
                        deleteTooltipMsg,
                        deleteCallBackFunction,
                        deleteModal,
                        detailMsg
                      }) => {

  // default 삭제 모달
  deleteModal ??= <DeleteModal
    url={url}
    path={path}
    title={deleteTitle}
    deleteCallBackFunction={deleteCallBackFunction}
    deleteTooltipMsg={deleteTooltipMsg}
    detailMsg={detailMsg}
  />

  return (
    <Row className={"justify-content-center"}>
      <Col md={16}>
        <Card>
          <Row className={"justify-content-between"}>
            <h4>{title}</h4>
            <Space>
              <EditOutlined
                style={{fontSize: "1.2rem", color: "green"}}
                onClick={onClickModifyBtn}/>
              {deleteModal}
            </Space>
          </Row>
          {tags}
          <div>
            {children}
          </div>
        </Card>
      </Col>
    </Row>

  );
}

export default DetailLayout;
