import GButton from "../../../components/button/GButton";
import React from "react";
import AddGoal from "./AddGoal";
import GoalCard from "./GoalCard";

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
      <div>
        <span style={{fontSize: "1.25rem", fontWeight: "bold"}} className="text-garden">나의 목표</span>
        {!isAddFormOpened
          ? <GButton color="garden" className="float-end" onClick={changeFormOpen}>추가</GButton> : <></>}
      </div>
      {/* 목표 추가 */}
      <div style={{marginBottom: "2.5rem"}}>
        {isAddFormOpened
          ? <AddGoal
            onClickGoalFormButton={changeFormOpen}
            addGoal={addGoal}
            plantList={plantList}/>
          : <></>}
      </div>

      {/* 목표 리스트 영역 */}
      <div>
        <div>
          {
            goalList.map((goal, idx) =>
              <GoalCard
                goal={goal}
                key={goal.id}
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
