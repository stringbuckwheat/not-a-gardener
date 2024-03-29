import {Space, Tag} from "antd";
import {useSelector} from "react-redux";

const PlantTag = ({plant, latestWateringDate}) => {
  const tmp = new Date();
  const today = new Date(tmp.getFullYear(), tmp.getMonth(), tmp.getDate());
  const totalWaterings = useSelector(state => state.waterings.totalWaterings);


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

  // 며칠 전에 물을 줬는지 계산
  const getLatestWateringDate = () => {
    const latestWateringDay = new Date(latestWateringDate);

    const diffTime = today.getTime() - latestWateringDay.getTime();
    const diffDate = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); // 올림

    return diffDate;
  }

  const calculateNextWateringDate = () => {
    const recentWateringPeriod = plant.recentWateringPeriod;

    // 마지막 물주기 날짜에서 recentWateringPeriod 더하고
    const nextWateringDate = new Date(latestWateringDate);
    nextWateringDate.setDate(nextWateringDate.getDate() + recentWateringPeriod);

    // 위 날짜에서 오늘 날짜를 뺸다
    const diffTime = nextWateringDate.getTime() - today.getTime();
    const diffDate = Math.floor(diffTime / (1000 * 60 * 60 * 24));

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
    if (plant.recentWateringPeriod == 0) {
      return "물주기 정보는 알아가는 중이에요"
    } else {
      return `${plant.recentWateringPeriod}일 간격으로 물을 마셔요`
    }
  }

  const isToday = (dateString) => {
    const diffTime = today.getTime() - new Date(dateString).getTime();
    const diffDate = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); // 올림
    return diffDate == 0;
  }

  const isNotDry = () => {
    if (isToday(plant.conditionDate)) {
      return <Tag color={"orange-inverse"}>오늘은 물이 마르지 않았어요</Tag>
    }
  }

  const isPostponed = () => {
    if (isToday(plant.postponeDate)) {
      return <Tag color={"orange-inverse"}>오늘 물주기를 미뤘어요</Tag>
    }
  }

  return (
    <Space size={[0, 8]} wrap>
      {
        plant.species
          ? <Tag color="green">{plant.species}</Tag> : <></>
      }

      <Tag color="gold">{plant.medium}</Tag>
      <Tag color="magenta">{plant.placeName}</Tag>
      <Tag color="geekblue">{plant.createDate}일부터 함께</Tag>
      <Tag color="purple">{getAvgWateringPeriodMsg()}</Tag>

      { // 물주기 정보가 존재하지 않을 시 렌더링하지 않음
        latestWateringDate
          ?
          <>
            <Tag color="magenta">{getLatestWateringDateMsg()}</Tag>
            {
              calculateNextWateringDate() && totalWaterings > 1
                ? <Tag color="orange">{calculateNextWateringDate()}</Tag>
                : <></>
            }
          </>
          : <></>
      }
      <Tag color="cyan">{totalWaterings == 0 ? "물주기 기록이 없어요" : `${totalWaterings}번 물을 줬어요`}</Tag>
      {isNotDry()}
      {isPostponed()}
    </Space>
  )
}

export default PlantTag;
