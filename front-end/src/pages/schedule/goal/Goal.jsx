import {CCard, CCardBody} from "@coreui/react";
import NoSchedule from "../../../components/empty/NoSchedule";
import AddGoal from "./AddGoal";
import {useState} from "react";
import GoalContent from "./GoalContent";

const Goal = ({goals, plantList, addGoal, completeGoal, deleteGoal}) => {
  const [isAddFormOpened, setIsAddFormOpen] = useState(false);
  const changeFormOpen = () => setIsAddFormOpen(!isAddFormOpened);

  return (
    <CCard className="p-4">
      <CCardBody>
        {
          goals.length == 0
            ?
            <>
              <NoSchedule
                isAddFormOpened={isAddFormOpened}
                title="목표"
                onClickShowAddForm={changeFormOpen}>
                <AddGoal
                  onClickGoalFormButton={changeFormOpen}
                  addGoal={addGoal}
                  plantList={plantList}/>
              </NoSchedule>
            </>
            :
            <GoalContent
              isAddFormOpened={isAddFormOpened}
              changeFormOpen={changeFormOpen}
              addGoal={addGoal}
              plantList={plantList}
              goalList={goals}
              completeGoal={completeGoal}
              deleteGoal={deleteGoal}
            />
        }
      </CCardBody>
    </CCard>
  )
}

export default Goal
