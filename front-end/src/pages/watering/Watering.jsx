import getData from "../../api/backend-api/common/getData";
import {useEffect, useState} from "react";
import dayjs from "dayjs";
import Loading from "../../components/data/Loading";
import {Badge, Calendar, ConfigProvider} from 'antd';
import 'dayjs/locale/ko';
import locale from "antd/es/date-picker/locale/ko_KR";
import {CContainer} from "@coreui/react";
import WateringDetail from "./WateringDetail";

const Watering = () => {
  dayjs.locale('ko');

  const [loading, setLoading] = useState(true);
  const [wateringList, setWateringList] = useState({});
  const onMountWatering = async () => {
    const month = new Date().getMonth() + 1;
    const res = await getData(`/watering/month/${month}`);

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
        {listData.map((item) => (<Badge key={item.content} status={item.type} text={item.content}/>))}
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
    const res = await getData(`/watering/month/${value.get("M") + 1}`);
    setWateringList(res);
  };

  const onAdd = (res) => {
    // 키가 wateringDate인 value를 가져와서 그 value에 res 추가
    let prevWatering = wateringList[res.wateringDate]; // array or undefined

    if (!prevWatering) {
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
    console.log("afterDelete", afterDelete);
    wateringList[watering.wateringDate] = afterDelete;
    console.log("wateringList", wateringList);
    setWateringList(() => ({...wateringList}));
    console.log("wateringDetail", wateringDetail);
    setWateringDetail(() => wateringDetail.filter(w => w.id !== watering.wateringId));
    console.log("wateringDetail handle 이후", wateringDetail);
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
      <CContainer className="bg-white">
        <h5 className="mt-2 mb-1 pt-3 text-garden">물주기 기록</h5>
        <WateringDetail
          onDelete={onDelete}
          isWateringFormOpened={isWateringFormOpened}
          setIsWateringFormOpened={setIsWateringFormOpened}
          onAdd={onAdd}
          selectedDate={selectedDate}
          wateringDetail={wateringDetail}/>
        <Calendar
          className="mt-1"
          locale={locale}
          dateCellRender={dateCellRender}
          onSelect={onSelect}
          onPanelChange={onPanelChange}
        />
      </CContainer>
    </ConfigProvider>
  );
}

export default Watering;
