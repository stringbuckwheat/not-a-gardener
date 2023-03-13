import { Space, Tag } from "antd";
const PlantTag = (props) => {
    const plant = props.plant;
    console.log("plant tag plant", plant);
    // const latestWateringDate = props.latestWateringDate.wateringDate;
    const wateringListSize = props.wateringListSize;
    const today = new Date();

    const getLatestWateringDateMsg = () => {
        const diffDate = getLatestWateringDate();

        if (diffDate == 0) {
            return "오늘 물을 줬어요!"
        } else if (diffDate == 1) {
            return "어제 물을 줬어요!"
        } else if (diffDate == 2) {
            return "이틀 전에 물을 줬어요!"
        } else if (diffDate == 7) {
            return "일주일 전에 물을 줬어요!"
        } else if (diffDate >= 3) {
            return `${diffDate}일 전에 물을 줬어요!`
        }
    }

    const getLatestWateringDate = () => {
        const latestWateringDate = new Date(props.latestWateringDate.wateringDate);
        const diffTime = today.getTime() - latestWateringDate.getTime();
        const diffDate = Math.floor(diffTime / (1000 * 60 * 60 * 24));

        return diffDate;
    }

    const calculateNextWateringDate = () => {
        const latestWateringDate = new Date(props.latestWateringDate.wateringDate);
        const averageWateringPeriod = plant.averageWateringPeriod;

        // 마지막 물주기 날짜에서 averageWateringPeriod 더하고
        const nextWateringDate = new Date(props.latestWateringDate.wateringDate);
        nextWateringDate.setDate(nextWateringDate.getDate() + averageWateringPeriod);

        // 위 날짜에서 오늘 날짜를 뺸다
        const diffTime = nextWateringDate.getTime() - today.getTime();
        const diffDate = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); // 얘는 올림

        if (diffDate == 0 && getLatestWateringDate() !== 0) {
            return "이 식물은 오늘 목이 마를 거예요"
        } else if (diffDate == 1) {
            return "이 식물은 내일 목이 마를 거예요"
        } else if (diffDate == 2) {
            return "이 식물은 이틀 뒤 목이 마를 거예요"
        } else if (diffDate == 7) {
            return "이 식물은 일주일 뒤 목이 마를 거예요"
        } else if (diffDate >= 3) {
            return `이 식물은 ${diffDate}일 뒤 목이 마를 거예요`
        } else if (diffDate < 0) {
            return "앗, 물주기를 놓쳤어요!"
        }
    }

    const getAvgWateringPeriodMsg = () => {
        if(plant.averageWateringPeriod == 0){
            return "물주기 정보는 알아가는 중이에요"
        } else {
            return `평균 물주기 ${plant.averageWateringPeriod}일`
        }
    }

    return (
        <Space size={[0, 8]} wrap>
            <Tag color="green">{plant.plantSpecies}</Tag>
            <Tag color="gold">{plant.medium}</Tag>
            <Tag color="magenta">{plant.placeName}</Tag>
            <Tag color="geekblue">{plant.createDate}일부터 함께</Tag>
            <Tag color="purple">{getAvgWateringPeriodMsg()}</Tag>
            { // 물주기 정보가 존재하지 않을 시 렌더링하지 않음
                props.latestWateringDate
                    ?
                    <>
                        <Tag color="magenta">{getLatestWateringDateMsg()}</Tag>
                        {
                            calculateNextWateringDate() && wateringListSize > 1
                            ? <Tag color="magenta">{calculateNextWateringDate()}</Tag>
                            : <></>
                        }
                        
                    </>
                    : <></>}
        </Space>
    )
}

export default PlantTag;