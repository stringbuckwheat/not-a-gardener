import {Tag, Space} from "antd";

const GardenTag = (props) => {
  const plant = props.plant.plant;
  const gardenDetail = props.plant.gardenDetail;

  const getLatestWateringDateMsg = () => {
    if (!gardenDetail.latestWateringDate) {
      return "물주기 기록이 없어요"
    }

    const today = new Date();
    const latestWateringDate = new Date(gardenDetail.latestWateringDate.wateringDate);
    const diffTime = today.getTime() - latestWateringDate.getTime();
    const diffDate = Math.floor(diffTime / (1000 * 60 * 60 * 24));

    if (diffDate == 0) {
      return "오늘 물을 마셨어요"
    } else if (diffDate == 1) {
      return "어제 물을 마셨어요"
    } else if (diffDate == 2) {
      return "이틀 전 물을 마셨어요"
    } else {
      return `${diffDate}일 전 물을 마셨어요`
    }
  }

  const getAverageWateringPeriodMsg = () => {
    if (plant.averageWateringPeriod == 0) {
      return "물주기를 함께 알아봐요";
    }

    return `${plant.averageWateringPeriod}일마다 물을 마셔요`
  }

  return (
    <div className="mt-2">
      <Space size={[0, 5]} wrap>
        <Tag>{getAverageWateringPeriodMsg()}</Tag>
        <Tag>{getLatestWateringDateMsg()}</Tag>
      </Space>
    </div>
  )
}

export default GardenTag;
