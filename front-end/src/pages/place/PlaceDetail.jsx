import {useParams} from 'react-router-dom'
import DetailLayout from 'src/components/data/layout/DetailLayout';
import PlaceTag from './PlaceTag';
import {useEffect, useState} from 'react';
import ModifyPlace from './ModifyPlace';
import PlaceTableForPlant from 'src/components/table/PlaceTableForPlant';
import DeletePlaceModal from "../../components/modal/DeletePlaceModal";
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";

const PlaceDetail = () => {
  const placeNo = useParams().placeNo;

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
    onMountPlaceDetail();
  }, []);

  if(loading){
    return <Loading />
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
