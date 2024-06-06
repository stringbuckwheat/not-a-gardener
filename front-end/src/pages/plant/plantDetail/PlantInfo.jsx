import {Col, Row} from "antd";
import {useSelector} from "react-redux";
import Style from "./PlantInfo.module.scss"

const PlantInfoItem = ({label, value}) => {
  return (
    <div className={Style.infoItem}>
      <div className={Style.label}>{label}</div>
      <div className={Style.separator}> |</div>
      <div className={Style.value}>{value}</div>
    </div>
  )
}

const PlantInfo = () => {
  const tmp = new Date();
  const today = new Date(tmp.getFullYear(), tmp.getMonth(), tmp.getDate());
  const plant = useSelector(state => state.plantDetail.detail);
  const status = useSelector(state => state.plantDetail.detail.status);
  const totalWaterings = useSelector(state => state.waterings.totalWaterings);

  const getLatestWateringDateMsg = () => {
    const diffDate = getLatestWateringPeriod();

    if (diffDate == 0) {
      return "오늘"
    } else if (diffDate == 1) {
      return "어제"
    } else if (diffDate == 2) {
      return "이틀 전"
    } else if (diffDate == 7) {
      return "일주일 전"
    } else if (diffDate >= 3) {
      return `${diffDate}일 전`
    }
  }

  // 며칠 전에 물을 줬는지 계산
  const getLatestWateringPeriod = () => {
    const latestWateringDay = new Date(plant.latestWateringDate);

    const diffTime = today.getTime() - latestWateringDay.getTime();
    const diffDate = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); // 올림

    return diffDate;
  }

  const calculateNextWateringDate = () => {
    const recentWateringPeriod = plant.recentWateringPeriod;

    // 마지막 물주기 날짜에서 recentWateringPeriod 더하고
    const nextWateringDate = new Date(plant.latestWateringDate);
    nextWateringDate.setDate(nextWateringDate.getDate() + recentWateringPeriod);

    // 위 날짜에서 오늘 날짜를 뺸다
    const diffTime = nextWateringDate.getTime() - today.getTime();
    const diffDate = Math.floor(diffTime / (1000 * 60 * 60 * 24));

    if (diffDate == 0 && getLatestWateringPeriod() !== 0) {
      return "오늘"
    } else if (diffDate == 1) {
      return "내일"
    } else if (diffDate == 2) {
      return "모레"
    } else if (diffDate == 7) {
      return "일주일 뒤"
    } else if (diffDate >= 3) {
      return `${diffDate}일 뒤`
    } else if (diffDate < 0) {
      return "물주기를 놓쳤어요!"
    }
  }

  const isToday = (dateString) => {
    const diffTime = today.getTime() - new Date(dateString).getTime();
    const diffDate = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); // 올림
    return diffDate == 0;
  }

  const isNotDry = () => {
    if (isToday(plant.conditionDate)) {
      return <div className={Style.value}>오늘은 물이 마르지 않았어요</div>
    }
  }

  const isPostponed = () => {
    if (isToday(plant.postponeDate)) {
      return <div className={Style.value}>오늘 물주기를 미뤘어요</div>
    }
  }

  console.log("status", status);

  return (
    <Row className={Style.plantInfoRow}>
      <Col md={12} xs={24} className={Style.plantInfoCol}>
        <PlantInfoItem label="식재 환경" value={plant.medium}/>
        <PlantInfoItem label="장소" value={plant.placeName}/>
        <PlantInfoItem label="키운 기간" value={plant.createDate}/>
        <PlantInfoItem
          label="최근 물주기"
          value={plant.recentWateringPeriod === 0 ? "알아가는 중" : `${plant.recentWateringPeriod}일 간격`}
        />
      </Col>
      <Col md={12} xs={24} className={Style.plantInfoCol}>
        {plant.latestWateringDate && (
          <>
            <PlantInfoItem label="마지막 관수일" value={getLatestWateringDateMsg()}/>
            {calculateNextWateringDate() && totalWaterings > 1 && (
              <PlantInfoItem label="예상 관수일" value={calculateNextWateringDate()}/>
            )}
          </>
        )}
        <PlantInfoItem
          label="물 준 횟수"
          value={totalWaterings === 0 ? "물주기 기록이 없어요" : `${totalWaterings}번`}
        />
        {isNotDry()}
        {isPostponed()}
      </Col>
    </Row>
  )
}

export default PlantInfo;
