import {Space, Tag} from 'antd';

const ChemicalTag = ({chemical, wateringListSize}) => {

  return (
    <Space size={[0, 8]} wrap>
      <Tag color="blue">{chemical.chemicalType}</Tag>
      <Tag color="green">{wateringListSize}번 사용했어요</Tag>
    </Space>
  )
}

export default ChemicalTag;
