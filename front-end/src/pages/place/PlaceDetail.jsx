import { useLocation, useParams } from 'react-router-dom'
import DetailLayout from 'src/components/data/layout/DetailLayout';
import PlaceTag from './PlaceTag';
import { useEffect, useState } from 'react';
import ModifyPlace from './ModifyPlace';
import onMount from 'src/api/service/onMount';
import DefaultTable from 'src/components/table/DefaultTable';
import plantTableColArrInPlace from "src/utils/dataArray/plantTableColArrInPlace";
import getPlantListForPlacePlantTable from 'src/utils/function/getPlantListForPlacePlantTable';

const PlaceDetail = () => {
  const path = useParams().placeNo;
  const { state } = useLocation();

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
    onMount(`/place/${state.placeNo}/plant-list`, setPlantList)
  }, [state]);

  return (
    !onModify
      ?
      <DetailLayout
        title={title}
        url="/place"
        path={state.placeNo}
        deleteTitle="장소"
        tags={<PlaceTag place={state} howManyPlant={plantList.length} />}
        onClickModifyBtn={onClickModifyBtn}
        deleteTooltipMsg="이 장소에 포함된 식물이 함께 삭제됩니다."
        bottomData={
          <DefaultTable
            path={path}
            columns={plantTableColArrInPlace}
            list={getPlantListForPlacePlantTable(plantList)}
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
        onClickGetBackBtn={onClickModifyBtn}
      />
  );
}

export default PlaceDetail;
