import {CCardGroup, CContainer} from '@coreui/react'
import Routine from "./routine/Routine";
import {useEffect, useState} from "react";
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";
import Goal from "./goal/Goal";
import {useDispatch} from "react-redux";

const Schedule = () => {
  const dispatch = useDispatch();

  const [loading, setLoading] = useState(true);
  const [routines, setRoutines] = useState({});
  const [goals, setGoals] = useState([]);

  const onMountSchedule = async () => {
    // 루틴
    const routineList = await getData("/routine");
    setRoutines(routineList);

    // 목표
    const goalList = await getData("/goal");
    setGoals(goalList);

    // 식물 리스트
    const data = await getData("/plant");
    dispatch({type: 'setPlants', payload: data});

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

  const deleteGoal = (goalId) => setGoals(() => goals.filter((goal) => goal.id !== goalId));

  return loading ? (
    <Loading/>
  ) : (
    <CContainer>
      <CCardGroup>
        <Routine routines={routines} />
        <Goal
          goals={goals}
          addGoal={addGoal}
          deleteGoal={deleteGoal}
          completeGoal={completeGoal}
        />
      </CCardGroup>
    </CContainer>
  )
}

export default Schedule;
