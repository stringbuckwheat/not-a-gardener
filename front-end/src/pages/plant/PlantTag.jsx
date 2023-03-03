import { Space, Tag } from "antd";
const PlantTag = (props) => {
    const plant = props.plant;

    return (
        <Space size={[0, 8]} wrap>
            <Tag color="green">{plant.plantSpecies}</Tag>
            <Tag color="volcano">{plant.placeName}</Tag>
            <Tag color="gold">{plant.medium}</Tag>
            <Tag color="geekblue">{plant.createDate}일부터 함께</Tag>
            <Tag color="purple">평균 물주기 {plant.averageWateringPeriod}일</Tag>
        </Space>
    )
}

export default PlantTag;