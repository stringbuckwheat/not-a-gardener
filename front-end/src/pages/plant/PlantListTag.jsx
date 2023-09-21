import {Space, Tag} from 'antd';
import {useSelector} from "react-redux";

const PlantListTag = () => {
  const plants = useSelector(state => state.plants);

  return (
    <Space size={[0, 8]} wrap>
      <Tag color="blue">{plants.length}개의 식물이 함께하고 있어요!</Tag>
    </Space>
  )
}

export default PlantListTag;
