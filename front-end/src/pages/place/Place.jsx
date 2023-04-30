import React, {useEffect, useState} from "react";
import NoItem from "src/components/empty/NoItem";
import PlaceList from "./PlaceList";
import getData from "src/api/backend-api/common/getData";
import Loading from "../../components/data/Loading";
import AddPlace from "./AddPlace";

/**
 * 장소 메인 페이지
 * @returns {JSX.Element}
 * @constructor
 */
const Place = () => {
  const [isLoading, setLoading] = useState(true);
  const [hasPlace, setHasPlace] = useState(false);
  const [placeList, setPlaceList] = useState([]);
  const [originPlaceList, setOriginPlaceList] = useState([]);

  const onMount = async () => {
    const data = await getData("/place");
    console.log("data", data);

    setLoading(false);
    setHasPlace(data.length > 0);
    setPlaceList(data);
    setOriginPlaceList(data);
  }

  useEffect(() => {
    onMount();
  }, [])

  const addPlace = (place) => {
    placeList.unshift(place);

    setPlaceList(placeList => placeList);
    setOriginPlaceList(placeList => placeList);
  }

  if (isLoading) {
    return <Loading/>
  } else if (!hasPlace) {
    return <NoItem
      title="등록된 장소가 없어요"
      buttonSize="lg"
      buttonTitle={"장소 추가하기"}
      addForm={<AddPlace addPlace={addPlace} afterAdd={() => setHasPlace(true)}/>}
    />
  } else {
    return <PlaceList
      placeList={placeList}
      setPlaceList={setPlaceList}
      originPlaceList={originPlaceList}
      setOriginPlaceList={setOriginPlaceList}
      addPlace={addPlace}
    />
  }
}

export default Place;
