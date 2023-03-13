import { Tag, Space } from "antd";

const GardenTag = (props) => {
    const plant = props.plant;

    const getAverageWateringPeriodMsg = () => {
        if(plant.averageWateringPeriod == 0){
            return "물주기를 함께 알아봐요";
        }

        return `${plant.averageWateringPeriod}일마다 물을 마셔요`
    }

    return (
        <div className="mt-3">
            <Space size={[0, 5]} wrap>
                <Tag>{plant.placeName}</Tag>
                <Tag>{getAverageWateringPeriodMsg()}</Tag>
                {
                    plant.anniversary !== ""
                    ?
                    <Tag>{plant.anniversary}</Tag>
                    : <></>
                }
            </Space>
        </div>
    )
}

export default GardenTag;