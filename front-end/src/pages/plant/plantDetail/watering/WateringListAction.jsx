import {Popconfirm, Space, Button} from "antd";
import {CloseOutlined, DeleteOutlined, EditOutlined} from "@ant-design/icons";

const WateringListAction = ({record, editable, updateWatering, editingKey, cancel, edit, deleteWatering}) => {
  return editable ? (
    <Space className="justify-content-end">
      <Button onClick={updateWatering} icon={<EditOutlined/>}/>
      <Popconfirm
        title="취소하시겠습니까?"
        okText="네"
        cancelText="아니요"
        onConfirm={cancel}>
        <Button icon={<CloseOutlined/>}/>
      </Popconfirm>
    </Space>
  ) : (
    <Space className="justify-content-end">
      <Button
        disabled={editingKey !== ''}
        onClick={() => edit(record)}
        icon={<EditOutlined/>}/>
      <Popconfirm
        placement="topRight"
        title="이 기록을 삭제하시겠습니까?"
        description="삭제한 물 주기 정보는 되돌릴 수 없어요"
        onConfirm={() => deleteWatering(record.id)}
        okText="네"
        cancelText="아니요"
      >
        <Button disabled={editingKey !== ''} icon={<DeleteOutlined/>}/>
      </Popconfirm>
    </Space>
  )
}

export default WateringListAction;
