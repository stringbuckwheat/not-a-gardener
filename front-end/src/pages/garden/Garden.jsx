import React, {useState, useEffect} from 'react'
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";
import GardenMain from "./GardenMain";
import wateringList from "../watering/WateringList";
import NoItemForPlant from "../../components/empty/NoItemForPlant";

const Garden = () => {
  const [isLoading, setLoading] = useState(true);
  const [nothingToDo, setNothingToDo] = useState(false);

  // 할일 리스트
  const [todoList, setTodoList] = useState([]);
  const [waitingList, setWaitingList] = useState([]);
  const [routineList, setRoutineList] = useState([]);

  const onMountGarden = async () => {
    const data = await getData("/garden"); // todoList, waitingList
    console.log("garden data", data);

    setLoading(false);
    setNothingToDo(data.todoList.length == 0 && data.waitingList.length == 0);

    data.todoList.sort((a, b) => (a.gardenDetail.wateringCode - b.gardenDetail.wateringCode));

    setTodoList(data.todoList);
    setWaitingList(data.waitingList);
    setRoutineList(data.routineList);
  }

  useEffect(() => {
    onMountGarden();
  }, [])

  const updateGardenAfterWatering = (gardenResponse) => {
    const updatedTodoList = todoList.filter((plant) => plant.plant.id !== gardenResponse.plant.id);
    setTodoList(() => [...updatedTodoList]);
  }

  // waiting리스트에서 삭제
  const updateWaitingListAfterWatering = (index) => {
    waitingList.splice(index, 1);
    setWaitingList(() => [...wateringList]);
  }

  // postpone
  const postponeWatering = (index, res) => {
    const handleTodoList = () => {
      const prevGarden = todoList.splice(index, 1)[0];
      const prevGardenDetail = prevGarden.gardenDetail;
      const newGardenDetail = {...prevGardenDetail, wateringCode: res};
      return [...todoList, {...prevGarden, gardenDetail: newGardenDetail}];
    }

    setTodoList(() => handleTodoList());
  }

  // 해당 인덱스의 루틴을 삭제
  const afterRoutine = (index, res) => {
    routineList.splice(index, 1, res);
    setRoutineList(() => routineList);
  }

  // 해당 인덱스의 식물을 waitingList와 todoList에서 삭제
  const deleteInWaitingListAndTodoList = (plantId) => {
    console.log("waitingList", waitingList);
    console.log("plantId", plantId);
    const updatedWaitingList = waitingList.filter((plant) => plant.id !== plantId);
    setWaitingList(() => [...updatedWaitingList]);
    const updatedTodoList = todoList.filter((plant) => plant.plant.id !== plantId);
    setTodoList(() => [...updatedTodoList]);
  }

  if (isLoading) {
    return <Loading/>
  } else if (nothingToDo) {
    return <NoItemForPlant afterAdd={onMountGarden} />
  } else {
    return (
      <GardenMain
        updateGardenAfterWatering={updateGardenAfterWatering}
        updateWaitingListAfterWatering={updateWaitingListAfterWatering}
        postponeWatering={postponeWatering}
        afterRoutine={afterRoutine}
        routineList={routineList}
        todoList={todoList}
        deleteInWaitingListAndTodoList={deleteInWaitingListAndTodoList}
        setTodoList={setTodoList}
        waitingList={waitingList}/>
    )
  }
}

export default Garden
