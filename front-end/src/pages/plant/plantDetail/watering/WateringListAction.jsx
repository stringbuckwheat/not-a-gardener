import {cilPen, cilTrash, cilX} from "@coreui/icons";
import CIcon from "@coreui/icons-react";
import {Popconfirm, Space, Button} from "antd";

const WateringListAction = ({record, editable, updateWatering, editingKey, cancel, edit, deleteWatering}) => {
  return editable ? (
    <Space className="d-flex justify-content-end">
      <Button onClick={updateWatering} size="small">
        <CIcon className="text-success" icon={cilPen}/>
      </Button>
      <Popconfirm
        title="취소하시겠습니까?"
        okText="네"
        cancelText="아니요"
        onConfirm={cancel}>
        <Button size="small">
          <CIcon icon={cilX}/>
        </Button>
      </Popconfirm>
    </Space>
  ) : (
    <Space className="d-flex justify-content-end">
      <Button
        size="small"
        disabled={editingKey !== ''}
        onClick={() => edit(record)}>
        <CIcon className="text-success" icon={cilPen}/>
      </Button>
      <Popconfirm
        placement="topRight"
        title="이 기록을 삭제하시겠습니까?"
        description="삭제한 물 주기 정보는 되돌릴 수 없어요"
        onConfirm={() => deleteWatering(record.id)}
        okText="네"
        cancelText="아니요"
      >
        <Button size="small" disabled={editingKey !== ''}>
          <CIcon icon={cilTrash}/>
        </Button>
      </Popconfirm>
    </Space>
  )
}

export default WateringListAction;
