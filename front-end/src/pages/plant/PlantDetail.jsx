import {useLocation, useParams} from 'react-router-dom'
import PlantTag from '../../components/tag/PlantTag';
import DetailLayout from 'src/components/data/layout/DetailLayout';
import {useState, useEffect} from 'react';
import ModifyPlant from './ModifyPlant';
import getPlaceList from 'src/utils/function/getPlaceList';
import WateringList from '../watering/WateringList';
import onMount from 'src/api/service/onMount';

const PlantDetail = () => {
  const plantNo = useParams().plantNo;

  const state = useLocation().state;

  const [plant, setPlant] = useState({});
  const [wateringList, setWateringList] = useState([{}]);

  useEffect(() => {
    onMount(`/plant/${plantNo}`, setPlant);
    onMount(`/plant/${plantNo}/watering`, setWateringList);
  }, [])

  useEffect(() => {
    if (state !== null) {
      setPlant(state);
    }
  }, [state])

  const [onModify, setOnModify] = useState(false);
  const [placeList, setPlaceList] = useState({});

  const onClickModifyBtn = async () => {
    const places = await getPlaceList();
    setPlaceList(places);
    setOnModify(!onModify);
  }

  const changeModifyState = () => {
    setOnModify(false);
  }

  return (
    !onModify
      ?
      <DetailLayout
        title={plant.plantName}
        url="/plant"
        path={plant.plantNo}
        deleteTitle="식물"
        tags={<PlantTag
          plant={plant}
          latestWateringDate={wateringList[0]}
          wateringListSize={wateringList.length}/>}
        onClickModifyBtn={onClickModifyBtn}
        bottomData={<WateringList plant={plant} setPlant={setPlant} wateringList={wateringList}
                                  setWateringList={setWateringList}/>}
      />
      :
      <ModifyPlant
        plant={plant}
        placeList={placeList}
        setOnModify={setOnModify}
        onClickGetBackBtn={onClickModifyBtn}
        changeModifyState={changeModifyState}
      />
  )
}

export default PlantDetail;
