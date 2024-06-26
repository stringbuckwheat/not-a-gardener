import NoContent from "../../../components/empty/NoContent";
import AddGoal from "./AddGoal";
import {useState} from "react";
import GoalContent from "./GoalContent";
import {Card} from "antd";

const Goal = ({goals, addGoal, completeGoal, deleteGoal}) => {
  const [isAddFormOpened, setIsAddFormOpen] = useState(false);
  const changeFormOpen = () => setIsAddFormOpen(!isAddFormOpened);

  return (
    <Card className="width-full" style={{margin: "0 1rem", padding: "1rem"}}>
      {
        goals.length == 0
          ?
          <>
            <NoContent
              isAddFormOpened={isAddFormOpened}
              title="목표"
              onClickShowAddForm={changeFormOpen}>
              <AddGoal
                onClickGoalFormButton={changeFormOpen}
                addGoal={addGoal}/>
            </NoContent>
          </>
          :
          <GoalContent
            isAddFormOpened={isAddFormOpened}
            changeFormOpen={changeFormOpen}
            addGoal={addGoal}
            goalList={goals}
            completeGoal={completeGoal}
            deleteGoal={deleteGoal}
          />
      }
    </Card>
  )
}

export default Goal
