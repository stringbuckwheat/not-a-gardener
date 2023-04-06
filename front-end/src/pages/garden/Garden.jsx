import React, {useState, useEffect} from 'react'
import NoItem from 'src/components/empty/NoItem';
import AddPlantButton from 'src/components/button/AddPlantButton';
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";
import GardenMain from "./GardenMain";

const Garden = () => {
  const [isLoading, setLoading] = useState(true);
  const [nothingToDo, setNothingToDo] = useState(false);

  // 할일 리스트
  const [todoList, setTodoList] = useState([]);
  const [waitingList, setWaitingList] = useState([]);
  const [routineList, setRoutineList] = useState([]);

  const onMountGarden = async () => {
    const data = await getData("/garden"); // todoList, waitingList

    setLoading(false);
    setNothingToDo(data.todoList.length == 0 && data.waitingList.length == 0);

    data.todoList.sort((a, b) => (a.gardenDetail.wateringDDay - b.gardenDetail.wateringDDay))

    setTodoList(data.todoList);
    setWaitingList(data.waitingList);
    setRoutineList(data.routineList);
  }

  useEffect(() => {
    onMountGarden();
  }, [])

  const updateGardenAfterWatering = (gardenResponse) => {
    const updatedTodoList = todoList.filter((plant) => plant.plant.plantNo !== gardenResponse.plant.plantNo);
    setTodoList(() => updatedTodoList);
  }

  if (isLoading) {
    return <Loading/>
  }
  else if (nothingToDo) {
    return <NoItem
      title="아직 정원에 아무도 없네요"
      button={<AddPlantButton size="lg"/>}/>
  }
  else {
    return (
      <GardenMain
        updateGardenAfterWatering={updateGardenAfterWatering}
        routineList={routineList}
        todoList={todoList}
        setTodoList={setTodoList}
        waitingList={waitingList}/>
    )
  }
}

export default Garden
