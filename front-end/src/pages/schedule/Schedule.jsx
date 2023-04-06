import {CCard, CCardBody, CCardGroup, CContainer} from '@coreui/react'
import Routine from "./routine/Routine";
import {useEffect, useState} from "react";
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";
import Goal from "./goal/Goal";

const Schedule = () => {
  const [loading, setLoading] = useState(true);
  const [routines, setRoutines] = useState({});
  const [goals, setGoals] = useState([]);
  const [plantList, setPlantList] = useState([]);

  const onMountSchedule = async () => {
    // 루틴
    const routineList = await getData("/routine");

    if(routineList.todoList){
      routineList.todoList.sort((a, b) => {
        if (a.isCompleted > b.isCompleted) return 1;
        if (a.isCompleted < b.isCompleted) return -1;
      });
    }

    setRoutines(routineList);

    // 목표
    const goalList = await getData("/goal");
    setGoals(goalList);

    // 식물 리스트
    const res = await getData("/plant");
    const plantList = res.map((plant) => (
      {
        label: `${plant.plantName} (${plant.placeName})`,
        value: plant.plantNo,
      }
    ))

    setPlantList(() => plantList);

    setLoading(false);
  }

  useEffect(() => {
    onMountSchedule();
  }, [])

  const addGoal = (goal) => {
    goals.unshift(goal);
    setGoals(() => goals);
  }

  const completeGoal = (index, goal) => {
    setGoals(() => {
      goals.splice(index, 1, goal);
      return goals
    })
  }

  const deleteGoal = (goalNo) => {
    setGoals(() => goals.filter((goal) => goal.goalNo !== goalNo));
  }

  if(loading){
    return <Loading />
  }

  return (
    <CContainer>
      <CCardGroup>
        <Routine routines={routines} plantList={plantList}/>
        <Goal goals={goals} plantList={plantList} addGoal={addGoal} deleteGoal={deleteGoal} completeGoal={completeGoal}/>
      </CCardGroup>
    </CContainer>
  )
}

export default Schedule;
