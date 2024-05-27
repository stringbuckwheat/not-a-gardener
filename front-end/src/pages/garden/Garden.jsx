import React, {useState, useEffect} from 'react'
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";
import GardenMain from "./GardenMain";
import NoItemForPlant from "../../components/empty/NoItemForPlant";
import {useDispatch} from "react-redux";

const Garden = () => {
  const [isLoading, setLoading] = useState(true);
  const [hasPlant, setHasPlant] = useState(false);

  const dispatch = useDispatch();

  const onMountGarden = async () => {
    console.log("onMountGarden");
    const data = await getData("/garden"); // todoList, waitingList
    console.log("garden data", data);

    setLoading(false);
    setHasPlant(data.hasPlant);

    dispatch({type: 'setTodoList', payload: data.todoList});
    dispatch({type: 'setWaitingList', payload: data.waitingList});
    dispatch({type: 'setRoutineList', payload: data.routineList});
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
