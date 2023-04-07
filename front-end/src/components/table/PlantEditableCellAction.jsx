import {Button, Popconfirm, Space} from "antd";
import CIcon from "@coreui/icons-react";
import {cilPen, cilTrash, cilX} from "@coreui/icons";

const PlantEditableCellAction = (props) => {
  const { record, editable, cancel, edit, editingKey, updatePlant, deletePlant } = props;

  return editable ? (
    <Space className="d-flex justify-content-end">
      <Button onClick={updatePlant} size="small">
        <CIcon className="text-success" icon={cilPen} />
      </Button>
      <Popconfirm
        title="취소하시겠어요?"
        okText="네"
        cancelText="아니요"
        onConfirm={cancel}>
        <Button size="small">
          <CIcon icon={cilX} />
        </Button>
      </Popconfirm>
    </Space>
  ) : (
    <Space className="d-flex justify-content-end">
      <Button
        size="small"
        disabled={editingKey !== ''}
        onClick={() => edit(record)}>
        <CIcon className="text-success" icon={cilPen} />
      </Button>
      <Popconfirm
        placement="topRight"
        title="이 식물을 삭제하실건가요?"
        description="해당 식물의 물주기 기록 등이 모두 함께 삭제됩니다."
        onConfirm={() => { deletePlant(record.plantNo) }}
        okText="네"
        cancelText="아니요"
      >
        <Button size="small" disabled={editingKey !== ''} >
          <CIcon icon={cilTrash} />
        </Button>
      </Popconfirm>
    </Space>
  )
}

export default PlantEditableCellAction;