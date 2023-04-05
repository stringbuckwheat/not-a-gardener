import {CCard, CCardBody} from "@coreui/react";
import React, {useState} from "react";
import getData from "../../../api/backend-api/common/getData";
import NoRoutine from "./NoRoutine";
import RoutineContent from "./RoutineContent";

const Routine = ({routines}) => {

  const [toDoList, setToDoList] = useState(routines.todoList);
  const [notToDoList, setNotToDoList] = useState(routines.notToDoList);
  const [plantList, setPlantList] = useState([]);

  const addRoutine = (routine) => {
    toDoList.unshift(routine);
    setToDoList(() => toDoList);
  }

  const deleteRoutine = (routineNo, hasToDoToday) => {
    if (hasToDoToday === "Y") {
      setToDoList(() => toDoList.filter((routine) => routine.routineNo !== routineNo));
      return;
    }

    setNotToDoList(() => notToDoList.filter((routine) => routine.routineNo !== routineNo));
  }

  const completeRoutine = (index, routine) => {
    setToDoList(() => {
      toDoList.splice(index, 1, routine);
      return toDoList;
    });
  }

  const [isRoutineFormOpened, setIsRoutineFormOpened] = useState(false);
  const onClickRoutineFormButton = () => {
    setIsRoutineFormOpened(!isRoutineFormOpened)
  }
  const onClickShowAddForm = async () => {
    setIsRoutineFormOpened(!isRoutineFormOpened);

    const res = await getData("/plant");
    const plantList = res.map((plant) => (
      {
        label: `${plant.plantName} (${plant.placeName})`,
        value: plant.plantNo,
      }
    ))

    setPlantList(() => plantList);
  }

  const sharedProps = {
    isRoutineFormOpened: isRoutineFormOpened,
    onClickRoutineFormButton: onClickRoutineFormButton,
    addRoutine: addRoutine,
    plantList: plantList,
    onClickShowAddForm: onClickShowAddForm
  }

  return (
    <CCard className="p-4">
      <CCardBody>
        {
          toDoList.length == 0 && notToDoList.length == 0
            ?
            <NoRoutine
              {...sharedProps}/>
            :
            <RoutineContent
              {...sharedProps}
              toDoList={toDoList}
              deleteRoutine={deleteRoutine}
              completeRoutine={completeRoutine}
              notToDoList={notToDoList}/>
        }
      </CCardBody>
    </CCard>
  )
}

export default Routine
