import {Checkbox, Popconfirm, Space, Tag} from "antd";
import {CloseOutlined} from "@ant-design/icons";
import GoalCardTag from "../tag/GoalCardTag";
import updateData from "../../api/backend-api/common/updateData";
import {useState} from "react";
import deleteData from "../../api/backend-api/common/deleteData";

const GoalCard = ({goal, index, completeGoal, deleteGoal}) => {
  const [isCompleted, setIsCompleted] = useState(goal.complete === "Y");

  const handleCheck = async (e) => {
    const res = await updateData(`/goal/${goal.goalNo}/complete`, null);
    completeGoal(index, res);
    setIsCompleted(e.target.checked);
  }

  const removeGoal = async () => {
    await deleteData(`/goal/${goal.goalNo}`);
    deleteGoal(goal.goalNo);
  }

  return (
    <div className="mb-3">
      <div>
        <Space>
          <Checkbox
            onChange={handleCheck}
            defaultChecked={isCompleted}
          />
          {
            isCompleted
              ? <span><del className="text-dark">{goal.goalContent}</del></span>
              : <span>{goal.goalContent}</span>
          }
        </Space>
        <Popconfirm
          title="이 목표를 삭제할까요?"
          description="목표를 삭제하면 복구할 수 없어요"
          onConfirm={removeGoal}
          okText="네"
          cancelText="아니요"
        >
          <CloseOutlined className="m-1 float-end text-dark" style={{fontSize: "0.9em"}}/>
        </Popconfirm>
      </div>
      <GoalCardTag isCompleted={isCompleted} plantName={goal.plantName} plantNo={goal.plantNo}/>
    </div>
  )
}

export default GoalCard;
