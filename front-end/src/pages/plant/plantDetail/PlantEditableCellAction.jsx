import {Button, Popconfirm, Space} from "antd";
import {CloseOutlined, DeleteOutlined, EditOutlined} from "@ant-design/icons";

// TODO WateringListAction과 같음

/**
 * Plant Table Row의 액션 버튼 렌더링
 * @param record
 * @param editable
 * @param cancel
 * @param edit
 * @param editingKey
 * @param updatePlant
 * @param deletePlant
 * @returns {JSX.Element}
 * @constructor
 */
const PlantEditableCellAction = ({record, editable, cancel, edit, editingKey, updatePlant, deletePlant}) => {
  const disabled = editingKey !== 0

  return editable ? (
    <Space className="justify-content-end">
      <Button onClick={updatePlant} icon={<EditOutlined/>}/>
      <Popconfirm
        title="취소하시겠어요?"
        okText="네"
        cancelText="아니요"
        onConfirm={cancel}>
        <Button icon={<CloseOutlined/>}/>
      </Popconfirm>
    </Space>
  ) : (
    <Space className="justify-content-end">
      <Button
        disabled={disabled}
        onClick={() => edit(record)}
        icon={<EditOutlined/>}/>
      <Popconfirm
        placement="topRight"
        title="이 식물을 삭제하실건가요?"
        description="해당 식물의 물주기 기록 등이 모두 함께 삭제됩니다."
        onConfirm={() => deletePlant(record.id)}
        okText="네"
        cancelText="아니요"
      >
        <Button disabled={disabled} icon={<DeleteOutlined/>}/>
      </Popconfirm>
    </Space>
  )
}

export default PlantEditableCellAction;
