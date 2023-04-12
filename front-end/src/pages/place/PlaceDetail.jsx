import {useLocation, useParams} from 'react-router-dom'
import DetailLayout from 'src/components/data/layout/DetailLayout';
import PlaceTag from './PlaceTag';
import {useEffect, useState} from 'react';
import ModifyPlace from './ModifyPlace';
import PlaceTableForPlant from 'src/pages/place/plant/PlantListInPlace';
import DeletePlaceModal from "../../components/modal/DeletePlaceModal";
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";

/**
 * 장소 상세정보
 * @returns {JSX.Element}
 * @constructor
 */
const PlaceDetail = () => {
  const placeNo = useParams().placeNo;
  const state = useLocation().state;

  const [loading, setLoading] = useState(true);
  const [place, setPlace] = useState({});
  const [plantList, setPlantList] = useState([]);

  // 수정 컴포넌트를 띄울지, 상세보기 컴포넌트를 띄울지
  const [onModify, setOnModify] = useState(false);

  // 부모 컴포넌트(PlaceDetail)의 state를 변경할 함수
  // 자식인 DetailLayout, ModifyPlace 컴포넌트로 넘겨준다(수정하기/돌아가기 버튼)
  const onClickModifyBtn = () => {
    setOnModify(!onModify);
  }

  const onMountPlaceDetail = async () => {
    const res = await getData(`/place/${placeNo}`);
    setPlace(res.place);
    setPlantList(res.plantList);
    setLoading(false);
  }

  useEffect(() => {
    if (state == null) {
      return;
    }

    if (!state.place && !state.plantList) {
      // 장소 수정
      setPlace(state);
      return;
    }

    // 다른 장소의 식물 이동
    setPlace(state.place);
    setPlantList(state.plantList);
  }, [state])

  useEffect(() => {
    onMountPlaceDetail();
  }, [placeNo]);

  if (loading) {
    return <Loading/>
  }

  return (
    !onModify
      ?
      <DetailLayout
        title={place.placeName}
        url="/place"
        path={place.placeNo}
        deleteTitle="장소"
        tags={<PlaceTag place={place} howManyPlant={plantList.length}/>}
        onClickModifyBtn={onClickModifyBtn}
        deleteModal={
          <DeletePlaceModal
            placeNo={placeNo}
            plantListSize={plantList.length}/>}
        bottomData={
          <PlaceTableForPlant
            placeName={place.placeName}
            plantList={plantList}
            setPlantList={setPlantList}
          />}
      />
      :
      <ModifyPlace
        title={place.placeName}
        place={{
          placeNo: place.placeNo,
          placeName: place.placeName,
          option: place.option,
          artificialLight: place.artificialLight
        }}
        changeModifyState={onClickModifyBtn}
      />
  );
}

export default PlaceDetail;
