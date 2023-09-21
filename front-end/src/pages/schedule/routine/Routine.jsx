import {CCard, CCardBody} from "@coreui/react";
import React, {useState} from "react";
import RoutineContent from "./RoutineContent";
import NoSchedule from "../../../components/empty/NoSchedule";
import AddRoutine from "./AddRoutine";

const Routine = ({routines}) => {
  const [toDoList, setToDoList] = useState(routines.todoList);
  const [notToDoList, setNotToDoList] = useState(routines.notToDoList);

  const addRoutine = (routine) => {
    toDoList.unshift(routine);
    setToDoList(() => toDoList);
  }

  const deleteRoutine = (routineId, hasToDoToday) => {
    if (hasToDoToday === "Y") {
      setToDoList(() => toDoList.filter((routine) => routine.id !== routineId));
      return;
    }

    setNotToDoList(() => notToDoList.filter((routine) => routine.id !== routineId));
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
    console.log("onClickRoutineFormButton", isRoutineFormOpened);
  }

  return (
    <CCard className="p-4">
      <CCardBody>
        {
          toDoList.length == 0 && notToDoList.length == 0
            ?
            <NoSchedule
              isAddFormOpened={isRoutineFormOpened}
              title="루틴"
              onClickShowAddForm={onClickRoutineFormButton}
            >
              <AddRoutine
                onClickRoutineFormButton={onClickRoutineFormButton}
                addRoutine={addRoutine}
              />
            </NoSchedule>
            :
            <RoutineContent
              isRoutineFormOpened={isRoutineFormOpened}
              onClickRoutineFormButton={onClickRoutineFormButton}
              addRoutine={addRoutine}

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
