import GButton from "../../../components/button/defaultButton/GButton";
import React from "react";
import AddGoal from "./AddGoal";
import GoalCard from "../../../components/card/GoalCard";

const GoalContent = ({
                       isAddFormOpened,
                       changeFormOpen,
                       plantList,
                       goalList,
                       addGoal,
                       completeGoal,
                       deleteGoal,
                     }) => {

  return (
    <>
      <div className="mb-4">
        <span className="fs-5 text-garden">나의 목표</span>
        {!isAddFormOpened
          ? <GButton color="garden" className="float-end" onClick={changeFormOpen}>추가</GButton> : <></>}
      </div>
      {/* 목표 추가 */}
      <div className="mb-5">
        {isAddFormOpened
          ? <AddGoal
            onClickGoalFormButton={changeFormOpen}
            addGoal={addGoal}
            plantList={plantList}/>
          : <></>}
      </div>

      {/* 목표 리스트 영역 */}
      <div>
        <div className="mb-2">
          {
            goalList.map((goal, idx) =>
              <GoalCard
                goal={goal}
                key={goal.goalNo}
                index={idx}
                completeGoal={completeGoal}
                deleteGoal={deleteGoal}
              />)
          }
        </div>
      </div>
    </>
  )
}

export default GoalContent;
