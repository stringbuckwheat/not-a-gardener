import React, {useEffect, useState} from "react";
import NoItem from "src/components/empty/NoItem";
import AddItemButton from "src/components/button/AddItemButton";
import PlaceList from "./PlaceList";
import getData from "src/api/backend-api/common/getData";
import Loading from "../../components/data/Loading";

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

    setLoading(false);
    setHasPlace(data.length > 0);
    setPlaceList(data);
    setOriginPlaceList(data);
  }

  useEffect(() => {
    onMount();
  }, [])

  if (isLoading) {
    return <Loading/>
  } else if (!hasPlace) {
    return <NoItem title="등록된 장소가 없어요" button={<AddItemButton addUrl="/place/add" size="lg" title="장소 추가하기"/>}/>
  } else {
    return <PlaceList
      placeList={placeList}
      setPlaceList={setPlaceList}
      originPlaceList={originPlaceList}
      setOriginPlaceList={setOriginPlaceList}
    />
  }
}

export default Place;
