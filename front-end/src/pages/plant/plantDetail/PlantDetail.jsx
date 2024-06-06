import {useLocation, useNavigate, useParams} from 'react-router-dom'
import PlantInfo from './PlantInfo';
import DetailLayout from 'src/components/data/layout/DetailLayout';
import {useState, useEffect} from 'react';
import ModifyPlant from '../ModifyPlant';
import getPlaceList from 'src/api/service/getPlaceList';
import getData from "../../../api/backend-api/common/getData";
import {useDispatch, useSelector} from "react-redux";
import ExceptionCode from "../../../utils/code/exceptionCode";
import PlantTitle from "../../../components/etc/PlantTitle";
import PlantLogTab from "./PlantLogTab";
import WateringStatusUpdate from "./WateringStatusUpdate";
import {notification} from "antd";
import AfterWateringCode from "../../../utils/code/afterWateringCode";
import getAfterWateringMsg from "../../../utils/function/getAfterWateringMsg";

/**
 * 식물 상세 정보 페이지 (해당 식물의 물주기 기록 포함)
 * @returns {JSX.Element} DetailLayout(PlantTag, WateringList) || ModifyPlant
 * @constructor
 */
const PlantDetail = () => {
  const plantId = useParams().plantId;
  const state = useLocation().state;
  const plant = useSelector(state => state.plantDetail.detail);

  const [onModify, setOnModify] = useState(false);

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const onMount = async () => {
    try {
      const res = await getData(`/plant/${plantId}`);
      console.log("res", res);
      dispatch({type: 'setPlantDetail', payload: res});
      dispatch({type: 'setTotalWaterings', payload: res.totalWatering});

      const places = await getData("/place");
      dispatch({type: 'setPlaces', payload: places});

    } catch (e) {
      if (e.code === ExceptionCode.NO_SUCH_PLANT) {
        alert("해당 식물을 찾을 수 없어요");
        navigate("/plant");
      }
    }
  }

  useEffect(() => {
    onMount();
  }, [])

  useEffect(() => {
    state && dispatch({type: "setPlantDetail", payload: state.plant});
  }, [state])

  const onClickModifyBtn = async () => {
    const places = await getPlaceList();
    dispatch({type: 'setPlaceListForSelect', payload: places});
    setOnModify(!onModify);
  }

  //////////////// status, watering update

  // 물주기 추가/수정/삭제 후 메시지
  const [api, contextHolder] = notification.useNotification();
  const openNotification = (msg) => {

    api.open({
      message: msg.title,
      description: msg.content,
      duration: 4,
    });
  };

  const wateringCallBack = (res) => {
    dispatch({type: "setWateringsForPlantDetail", payload: res.waterings});
    res.plant && dispatch({type: "setPlantDetail", payload: res.plant});

    if (AfterWateringCode.POSSIBLE_HEAVY_DRINKER == res.wateringMsg?.afterWateringCode) {
      console.log("heavy drinker");
      dispatch({type: "setHeavyDrinkerCheck", payload: true});
    } else if (res.wateringMsg) {
      const msg = getAfterWateringMsg(res.wateringMsg.afterWateringCode);
      openNotification(msg);
    }
  }

  return (
    <>
      {contextHolder}

      {
        !onModify
          ?
          <DetailLayout
            title={<PlantTitle name={plant.name} species={plant.species} status={plant.status}/>}
            url="/plant"
            path={plant.id}
            deleteTitle="식물"
            tags={<PlantInfo/>}
            onClickModifyBtn={onClickModifyBtn}
          >
            <WateringStatusUpdate wateringCallBack={wateringCallBack} openNotification={openNotification}/>
            <PlantLogTab wateringCallBack={wateringCallBack} openNotification={openNotification}/>
          </DetailLayout>
          :
          <ModifyPlant changeModifyState={() => setOnModify(false)}/>
      }
    </>
  )
}

export default PlantDetail;
