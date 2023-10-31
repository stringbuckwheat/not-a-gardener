import {Space, Tag} from 'antd';

const ChemicalTag = ({chemical, wateringListSize}) => {
  return (
    <Space size={[0, 8]} wrap>
      <Tag color="blue">{chemical.type}</Tag>
      <Tag color="gold"><b>{chemical.period}</b>{`일에 한 번 사용해요`}</Tag>
      <Tag color="green">{wateringListSize}번 사용했어요</Tag>
    </Space>
  )
}

export default ChemicalTag;
