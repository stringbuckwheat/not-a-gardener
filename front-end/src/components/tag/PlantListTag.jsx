import { Space, Tag } from 'antd';

const PlantListTag = (props) => {
    const howManyPlants = props.howManyPlants;

    return(
        <Space size={[0, 8]} wrap>
            <Tag color="blue">{howManyPlants}개의 식물이 함께하고 있어요!</Tag>
        </Space>
    )
}

export default PlantListTag;
