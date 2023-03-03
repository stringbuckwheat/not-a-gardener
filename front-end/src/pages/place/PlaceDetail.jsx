import { useLocation } from 'react-router-dom'
import SmartTable from 'src/components/table/SmartTable';
import DetailLayout from 'src/components/form/DetailLayout';
import PlaceTag from './PlaceTag';

const PlaceDetail = () => {
  // props
  const { state } = useLocation();
  const title = state.placeName;

  const tableHeadArr = ["식물 이름", "종", "식재 환경", "평균 물주기", "createDate"];
  const keySet = ["plantName", "plantSpecies", "medium", "averageWateringPeriod", "createDate"]

  return (
      <DetailLayout
        title={title}
        tags={<PlaceTag place={state}/>}
        bottomData={<SmartTable list={state.plantList} />}
      />
  );
}

export default PlaceDetail;