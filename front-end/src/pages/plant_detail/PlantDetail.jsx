import {useLocation, useNavigate, useParams} from 'react-router-dom'
import PlantInfo from './PlantInfo';
import DetailLayout from 'src/components/data/layout/DetailLayout';
import {useState, useEffect} from 'react';
import ModifyPlant from '../plant/ModifyPlant';
import getPlaceList from 'src/api/service/getPlaceList';
import getData from "../../api/backend-api/common/getData";
import {useDispatch, useSelector} from "react-redux";
import ExceptionCode from "../../utils/code/exceptionCode";
import PlantTitle from "../../components/etc/PlantTitle";
import PlantLogTab from "./PlantLogTab";
import WateringStatusUpdate from "./WateringStatusUpdate";
import {notification} from "antd";
import AfterWateringCode from "../../utils/code/afterWateringCode";
import getAfterWateringMsg from "../../utils/function/getAfterWateringMsg";
import PlantDetailAction from "../../redux/reducer/plant_detail/plantDetailAction";
import WateringAction from "../../redux/reducer/waterings/wateringAction";
import Loading from "../../components/data/Loading";

/**
 * 식물 상세 정보 페이지 (해당 식물의 물주기 기록 포함)
 * @returns {JSX.Element} DetailLayout(PlantTag, WateringList) || ModifyPlant
 * @constructor
 */
const PlantDetail = () => {
  const plantId = useParams().plantId;
  const state = useLocation().state;
  const plant = useSelector(state => state.plantDetail.detail);

  const [isLoading, setIsLoading] = useState(true);
  const [onModify, setOnModify] = useState(false);

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const onMount = async () => {
    try {
      const res = await getData(`/plant/${plantId}`);
      console.log("---------- res", res);
      dispatch({type: PlantDetailAction.FETCH_PLANT_DETAIL, payload: res});
      dispatch({type: WateringAction.FETCH_TOTAL_WATERING, payload: res.totalWatering});
      setIsLoading(false);
    } catch (e) {
      if (e.code === ExceptionCode.NO_SUCH_PLANT) {
        alert("해당 식물을 찾을 수 없어요");
      }

      navigate("/plant");
    }
  }

  useEffect(() => {
    onMount();
  }, [])

  useEffect(() => {
    state && dispatch({type: PlantDetailAction.FETCH_PLANT_DETAIL, payload: state.plant});
  }, [state])

  const onClickModifyBtn = async () => {
    const places = await getPlaceList();
    console.log("places", places);
    dispatch({type: PlantDetailAction.SET_PLACES_FOR_SELECT, payload: places});
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
    dispatch({type: PlantDetailAction.FETCH_WATERING, payload: res.waterings});
    res.plant && dispatch({type: PlantDetailAction.FETCH_PLANT_DETAIL, payload: res.plant});

    if (AfterWateringCode.POSSIBLE_HEAVY_DRINKER == res.wateringMsg?.afterWateringCode) {
      dispatch({type: PlantDetailAction.SET_HEAVY_DRINKER_CHECK, payload: true});
    } else if (res.wateringMsg) {
      const msg = getAfterWateringMsg(res.wateringMsg.afterWateringCode);
      openNotification(msg);
    }
  }

  return isLoading ? (<Loading/>) : (
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
            info={<PlantInfo/>}
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
