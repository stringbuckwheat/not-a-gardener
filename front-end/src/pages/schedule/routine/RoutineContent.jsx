import GButton from "../../../components/button/GButton";
import AddRoutine from "./AddRoutine";
import RoutineCard from "./RoutineCard";
import {Row} from "antd";
import React from "react";

const RoutineContent = ({
                              isRoutineFormOpened,
                              onClickRoutineFormButton,
                              addRoutine,
                              toDoList,
                              deleteRoutine,
                              completeRoutine,
                              notToDoList
                            }) => {
  const style = {fontSize: "0.8em"}

  return (
    <>
      <div className="mb-4">
        <span className="fs-5 text-garden">나의 루틴</span>
        {!isRoutineFormOpened
          ? <GButton color="garden" className="float-end" onClick={onClickRoutineFormButton}>추가</GButton> : <></>}
      </div>
      {/* 루틴 추가 */}
      <div className="mb-4">
        {isRoutineFormOpened
          ? <AddRoutine
            onClickRoutineFormButton={onClickRoutineFormButton}
            addRoutine={addRoutine}/>
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
                key={routine.id}
                index={idx}
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
    </>
  )
}

export default RoutineContent;
