import {useLocation, useNavigate, useParams} from 'react-router-dom'
import PlantTag from './PlantTag';
import DetailLayout from 'src/components/data/layout/DetailLayout';
import {useState, useEffect} from 'react';
import ModifyPlant from '../ModifyPlant';
import getPlaceList from 'src/api/service/getPlaceList';
import WateringList from './watering/WateringList';
import getData from "../../../api/backend-api/common/getData";

/**
 * 식물 상세 정보 페이지 (해당 식물의 물주기 기록 포함)
 * @returns {JSX.Element} DetailLayout(PlantTag, WateringList) || ModifyPlant
 * @constructor
 */
const PlantDetail = () => {
  const plantId = useParams().plantId;
  const state = useLocation().state;

  const [plant, setPlant] = useState({});
  const [wateringList, setWateringList] = useState([{}]);

  const navigate = useNavigate();

  const onMount = async () => {
    try{
      const res = await getData(`/plant/${plantId}`);
      setPlant(res.plant);
      setWateringList(res.waterings);
    } catch (e) {
      if(e.code === "B006"){
        alert("해당 식물을 찾을 수 없어요");
        navigate("/plant");
      }
    }
  }

  useEffect(() => {
    onMount();
  }, [])

  useEffect(() => {
    state && setPlant(state.plant);
  }, [state])

  const [onModify, setOnModify] = useState(false);
  const [placeList, setPlaceList] = useState({});

  const onClickModifyBtn = async () => {
    const places = await getPlaceList();
    setPlaceList(places);
    setOnModify(!onModify);
  }

  return (
    !onModify
      ?
      <DetailLayout
        title={plant.name}
        url="/plant"
        path={plant.id}
        deleteTitle="식물"
        tags={<PlantTag
          plant={plant}
          latestWateringDate={wateringList[0]}
          wateringListSize={wateringList.length}/>}
        onClickModifyBtn={onClickModifyBtn}
        bottomData={<WateringList
          plant={plant}
          setPlant={setPlant}
          wateringList={wateringList}
          setWateringList={setWateringList}/>}
      />
      :
      <ModifyPlant
        plant={plant}
        placeList={placeList}
        changeModifyState={() => setOnModify(false)}
      />
  )
}

export default PlantDetail;
