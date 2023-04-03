import {useLocation} from 'react-router-dom'
import DetailLayout from 'src/components/data/layout/DetailLayout';
import PlaceTag from './PlaceTag';
import {useEffect, useState} from 'react';
import ModifyPlace from './ModifyPlace';
import onMount from 'src/api/service/onMount';
import PlaceTableForPlant from 'src/components/table/PlaceTableForPlant';
import DeletePlaceModal from "../../components/modal/DeletePlaceModal";

const PlaceDetail = () => {
  const {state} = useLocation();

  const [plantList, setPlantList] = useState([{
    placeNo: 0,
    placeName: "",
    plantNo: 0,
    plantName: "",
    plantSpecies: "",
    medium: "",
    averageWateringPeriod: 0,
    createDate: ""
  }])

  const title = state.placeName; // 장소 이름

  // 수정 컴포넌트를 띄울지, 상세보기 컴포넌트를 띄울지
  const [onModify, setOnModify] = useState(false);

  // 부모 컴포넌트(PlaceDetail)의 state를 변경할 함수
  // 자식인 DetailLayout, ModifyPlace 컴포넌트로 넘겨준다(수정하기/돌아가기 버튼)
  const onClickModifyBtn = () => {
    setOnModify(!onModify);
  }

  useEffect(() => {
    // plant list에 쓸 정보를 받아옴
    onMount(`/place/${state.placeNo}/plants`, setPlantList)
  }, [state]);

  return (
    !onModify
      ?
      <DetailLayout
        title={title}
        url="/place"
        path={state.placeNo}
        deleteTitle="장소"
        tags={<PlaceTag place={state} howManyPlant={plantList.length}/>}
        onClickModifyBtn={onClickModifyBtn}
        deleteModal={
          <DeletePlaceModal
            placeNo={state.placeNo}
            plantListSize={plantList.length}/>}
        bottomData={
          <PlaceTableForPlant
            plantList={plantList}
            setPlantList={setPlantList}
          />}
      />
      :
      <ModifyPlace
        title={title}
        place={{
          placeNo: state.placeNo,
          placeName: state.placeName,
          option: state.option,
          artificialLight: state.artificialLight
        }}
        changeModifyState={onClickModifyBtn}
      />
  );
}

export default PlaceDetail;
