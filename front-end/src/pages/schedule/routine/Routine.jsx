import {CCard, CCardBody} from "@coreui/react";
import GButton from "../../../components/button/defaultButton/GButton";
import {useEffect, useState} from "react";
import AddRoutine from "./AddRoutine";
import getData from "../../../api/backend-api/common/getData";
import RoutineCard from "./RoutineCard";
import {Row} from "antd";

const Routine = () => {
  const style = {fontSize: "0.8em"}

  const [toDoList, setToDoList] = useState([]);
  const [notToDoList, setNotToDoList] = useState([]);
  const [plantList, setPlantList] = useState([]);

  const onMountRoutine = async () => {
    const res = await getData("/routine");
    console.log("res", res);

    res.todoList.sort((a, b) => {
      if(a.isCompleted > b.isCompleted) return 1;
      if(a.isCompleted < b.isCompleted) return -1;
    });

    setToDoList(res.todoList);
    setNotToDoList(res.notToDoList);
  }

  useEffect(() => {
    onMountRoutine()
  }, []);

  const addRoutine = (routine) => {
    console.log("add routine", routine);
    toDoList.unshift(routine);
    setToDoList(() => toDoList);
  }

  const deleteRoutine = (routineNo, hasToDoToday) => {
    if(hasToDoToday === "Y"){
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

  return (
    <CCard className="p-4">
      <CCardBody>
        <div className="mb-4">
          <span className="fs-5 text-garden">나의 루틴</span>
          {!isRoutineFormOpened
            ? <GButton color="garden" className="float-end" onClick={onClickShowAddForm}>추가</GButton> : <></>}
        </div>
        {/* 루틴 추가 */}
        <div className="mb-4">
          {isRoutineFormOpened
            ? <AddRoutine
              onClickRoutineFormButton={onClickRoutineFormButton}
              addRoutine={addRoutine}
              plantList={plantList}/>
            : <></>}
        </div>

        {/* 루틴 리스트 영역 */}
        <div>
          <div style={style}>오늘 할 일</div>
          <div className="mb-2">
            {
              toDoList.map((routine, idx) =>
                <RoutineCard
                  routine={routine}
                  key={idx}
                  deleteRoutine={deleteRoutine}
                  completeRoutine={completeRoutine}/>)
            }
          </div>
          <Row>
            <hr/>
          </Row>
          <div style={style}>기간이 남은 일</div>
          {
            notToDoList.map((routine, idx) =>
              <RoutineCard
                routine={routine}
                key={idx}
                deleteRoutine={deleteRoutine}/>)
          }
        </div>
      </CCardBody>
    </CCard>
  )
}

export default Routine
