import {useLocation, useNavigate, useParams} from 'react-router-dom'
import DetailLayout from 'src/components/data/layout/DetailLayout';
import PlaceTag from './PlaceTag';
import {useEffect, useState} from 'react';
import ModifyPlace from './ModifyPlace';
import PlaceTableForPlant from 'src/pages/place/plant/PlaceTableForPlant';
import DeletePlaceModal from "../../components/modal/DeletePlaceModal";
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";
import ExceptionCode from "../../utils/code/exceptionCode";

/**
 * 장소 상세정보
 * @returns {JSX.Element}
 * @constructor
 */
const PlaceDetail = () => {
  const placeId = useParams().placeId;
  const state = useLocation().state;

  const [loading, setLoading] = useState(true);
  const [place, setPlace] = useState({});

  // 수정 컴포넌트를 띄울지, 상세보기 컴포넌트를 띄울지
  const [onModify, setOnModify] = useState(false);

  const navigate = useNavigate();

  // 부모 컴포넌트(PlaceDetail)의 state를 변경할 함수
  // 자식인 DetailLayout, ModifyPlace 컴포넌트로 넘겨준다(수정하기/돌아가기 버튼)
  const onClickModifyBtn = () => setOnModify(!onModify);

  const onMountPlaceDetail = async () => {
    try {
      const res = await getData(`/place/${placeId}`);
      console.log("res", res);
      setPlace({...res});
      setLoading(false);
    } catch (e) {
      if (e.code === ExceptionCode.NO_SUCH_PLACE) {
        alert("해당 장소를 찾을 수 없어요");
        navigate("/place");
      }
    }
  }

  useEffect(() => {
    onMountPlaceDetail();
  }, [])

  useEffect(() => {
    console.log("state", state);

    if (state == null) {
      return;
    }

    console.log("장소수정")
    console.log("state", state);
    onMountPlaceDetail();
    setPlace(state);
  }, [state])

  if (loading) {
    return <Loading/>
  }

  return (
    !onModify
      ?
      <DetailLayout
        title={place.name}
        url="/place"
        path={placeId}
        deleteTitle="장소"
        tags={<PlaceTag place={place} howManyPlant={place.plantListSize}/>}
        onClickModifyBtn={onClickModifyBtn}
        deleteModal={
          <DeletePlaceModal
            placeId={place.id}
            plantListSize={place.plantListSize}/>}
        children={
          <PlaceTableForPlant
            placeId={placeId}
            plantListSize={place.plantListSize}
            placeName={place.name}
          />}
      />
      :
      <ModifyPlace
        title={place.name}
        place={{
          id: place.id,
          name: place.name,
          option: place.option,
          artificialLight: place.artificialLight
        }}
        changeModifyState={onClickModifyBtn}
      />
  );
}

export default PlaceDetail;
