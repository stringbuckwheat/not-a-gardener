import React, {useEffect, useState} from "react";
import NoItem from "src/components/empty/NoItem";
import PlaceList from "./PlaceList";
import getData from "src/api/backend-api/common/getData";
import Loading from "../../components/data/Loading";
import AddPlace from "./AddPlace";
import {useDispatch} from "react-redux";

/**
 * 장소 메인 페이지
 * @returns {JSX.Element}
 * @constructor
 */
const Place = () => {
  const [isLoading, setLoading] = useState(true);
  const [hasPlace, setHasPlace] = useState(false);

  const dispatch = useDispatch();

  const onMount = async () => {
    const data = await getData("/place");
    console.log("data", data);

    dispatch({type: 'setPlaces', payload: data});

    setLoading(false);
    setHasPlace(data.length > 0);
  }

  useEffect(() => {
    onMount();
  }, [])

  if (isLoading) {
    return <Loading/>
  } else if (!hasPlace) {
    return <NoItem
      title="등록된 장소가 없어요"
      buttonSize="lg"
      buttonTitle={"장소 추가하기"}
      addForm={<AddPlace afterAdd={() => setHasPlace(true)}/>}
    />
  } else {
    return <PlaceList />
  }
}

export default Place;
