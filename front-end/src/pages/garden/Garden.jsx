import React, {useState, useEffect} from 'react'
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";
import GardenMain from "./GardenMain";
import NoItemForPlant from "../../components/empty/NoItemForPlant";
import {useDispatch} from "react-redux";
import GardenAction from "../../redux/reducer/garden/gardenAction";

const Garden = () => {
  const [isLoading, setLoading] = useState(true);
  const [hasPlant, setHasPlant] = useState(false);

  const dispatch = useDispatch();

  const onMountGarden = async () => {
    console.log("onMountGarden");
    const res = await getData("/garden"); // todoList, waitingList
    console.log("garden data", res);

    setLoading(false);
    setHasPlant(res.hasPlant);

    dispatch({type: GardenAction.FETCH_GARDEN, payload: res});
  }

  useEffect(() => {
    onMountGarden();
  }, [])

  if (isLoading) {
    return <Loading/>
  } else if (!hasPlant) {
    return <NoItemForPlant afterAdd={onMountGarden}/>
  } else {
    return (
      <GardenMain />
    )
  }
}

export default Garden
