import { Space, Tag } from 'antd';

const ChemicalTag = (props) => {
    const chemical = props.chemical;
    console.log('chemical', chemical);
    // const howManyWatering = props.howManyWatering;
    const wateringListSize = props.wateringListSize;

    return (
        <Space size={[0, 8]} wrap>
            <Tag color="blue">{chemical.chemicalType}</Tag>
            <Tag color="green">{wateringListSize}번 사용했어요</Tag>
        </Space>
    )
}

export default ChemicalTag;