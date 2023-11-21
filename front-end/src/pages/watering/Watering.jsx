import getData from "../../api/backend-api/common/getData";
import {useEffect, useState} from "react";
import dayjs from "dayjs";
import Loading from "../../components/data/Loading";
import {Badge, Calendar, ConfigProvider} from 'antd';
import 'dayjs/locale/ko';
import locale from "antd/es/date-picker/locale/ko_KR";
import WateringDetail from "./WateringDetail";

const Watering = () => {
  dayjs.locale('ko');

  const [loading, setLoading] = useState(true);
  const [wateringList, setWateringList] = useState({});

  const getDate = (date) => {
    return date.toISOString().split("T").at(0);
  }
  const onMountWatering = async () => {
    const res = await getData(`/watering/date/${getDate(new Date())}`);

    console.log("res", res);

    setWateringList(res);
    setLoading(false);

    console.log("wateringList", res);
  }

  useEffect(() => {
    onMountWatering();
  }, [])

  const getListData = (day) => {
    // value == 현재 render되는 날짜
    const format = day.format("YYYY-MM-DD");

    if (wateringList[format]) {
      const listData = wateringList[format].map((watering) => ({
        type: 'warning', content: watering.plantName
      }))

      return listData;
    }

    return [];
  };

  const dateCellRender = (value) => {
    const listData = getListData(value);
    return (
      <>
        {listData.map((item) => (<div><Badge key={item.content} status={item.type} text={item.content}/></div>))}
      </>
    );
  };

  const today = dayjs();
  const [wateringDetail, setWateringDetail] = useState(() => wateringList[today.format("YYYY-MM-DD")]);
  const [selectedDate, setSelectedDate] = useState(() => today);
  const [isWateringFormOpened, setIsWateringFormOpened] = useState(false);

  const onSelect = (selectedDate) => {
    const format = selectedDate.format("YYYY-MM-DD");
    setWateringDetail(() => wateringList[format]);
    setSelectedDate(selectedDate);
    setIsWateringFormOpened(false);
  };

  const onPanelChange = async (value) => {
    const res = await getData(`/watering/date/${getDate(new Date(value))}`);
    setWateringList(res);
  };

  const onAdd = (res) => {
    // 키가 wateringDate인 value를 가져와서 그 value에 res 추가
    console.log("onAdd res", res);
    let prevWatering = wateringList[res.wateringDate]; // array or undefined
    console.log("prevWatering", prevWatering);

    if (!prevWatering) {
      console.log("prevWatering 없음")
      wateringList[res.wateringDate] = [{...res}];

      setWateringList(() => ({...wateringList}));
      setIsWateringFormOpened(false);
      return;
    }

    prevWatering.push(res);
    setWateringList(() => wateringList);
    setIsWateringFormOpened(false);
  }

  const onDelete = (watering) => {
    const afterDelete = wateringList[watering.wateringDate].filter(w => w.id !== watering.wateringId);
    wateringList[watering.wateringDate] = afterDelete;
    setWateringList(() => ({...wateringList}));
    setWateringDetail(() => wateringDetail.filter(w => w.id !== watering.wateringId));
  }

  useEffect(() => {
    !loading && setWateringDetail(() => wateringList[today.format("YYYY-MM-DD")]);
    // if (!loading) {
    //   setWateringDetail(() => wateringList[today.format("YYYY-MM-DD")]);
    // }
  }, [loading])

  return loading ? (
    <Loading/>
  ) : (
    <ConfigProvider theme={{token: {colorPrimary: '#6d9773'}}}>
      <div className="bg-white" style={{padding: "1rem 1.5rem"}}>
        <h4 className={"text-garden"}>물주기 기록</h4>
        <WateringDetail
          onDelete={onDelete}
          isWateringFormOpened={isWateringFormOpened}
          setIsWateringFormOpened={setIsWateringFormOpened}
          onAdd={onAdd}
          selectedDate={selectedDate}
          wateringDetail={wateringDetail}/>
        <Calendar
          style={{marginTop: "1rem", padding: "1rem"}}
          locale={locale}
          dateCellRender={dateCellRender}
          onSelect={onSelect}
          onPanelChange={onPanelChange}
        />
      </div>
    </ConfigProvider>
  );
}

export default Watering;
