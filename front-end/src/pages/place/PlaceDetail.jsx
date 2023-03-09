import { useLocation } from 'react-router-dom'
import DetailLayout from 'src/components/form/DetailLayout';
import PlaceTag from './PlaceTag';
import { useEffect, useState } from 'react';
import ModifyPlace from './ModifyPlace';
import authAxios from 'src/utils/interceptors';
import PlantListInPlaceTable from './PlantListInPlacetable';

const PlaceDetail = () => {
  // props
  const { state } = useLocation();
  console.log("placeDetail props", state);
  const [ plantList, setPlantList ] = useState([{
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

  const [onModify, setOnModify] = useState(false);
  const onClickModifyBtn = () => {
    setOnModify(!onModify);
  }

  useEffect(() => {
    authAxios.get(`/place/${state.placeNo}/plant-list`)
    .then((res) => {
      console.log("res.data", res.data);
      setPlantList(res.data);
    })
  }, []);

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
        bottomData={<PlantListInPlaceTable list={plantList}/>}
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