import { Space, Tag } from 'antd';

const PesticideTag = (props) => {
    const pesticide = props.pesticide;

    const getPesticideTypeColor = () => {
        if(pesticide.type === "살충제"){
            return "blue";
        } else if(pesticide.type === "살균제"){
            return "gold"
        } else if(pesticide.type === "천적 방제"){
            return "green"
        }
    }
    
    return(
        <Space size={[0, 8]} wrap>
            <Tag color="blue">{pesticide.pesticideType}</Tag>
            <Tag color="gold">{pesticide.pesticidePeriod}일에 한 번</Tag>
            <Tag color="volcano">xx일 남았어요</Tag>
        </Space>
    )
}

export default PesticideTag;