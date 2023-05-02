import {Checkbox, Popconfirm, Space, Tag} from "antd";
import {CloseOutlined} from "@ant-design/icons";
import GoalCardTag from "../../../components/tag/GoalCardTag";
import updateData from "../../../api/backend-api/common/updateData";
import {useState} from "react";
import deleteData from "../../../api/backend-api/common/deleteData";

/**
 * 목표 카드
 * @param goal
 * @param index
 * @param completeGoal
 * @param deleteGoal
 * @returns {JSX.Element}
 * @constructor
 */
const GoalCard = ({goal, index, completeGoal, deleteGoal}) => {
  console.log("goal", goal);
  const [isCompleted, setIsCompleted] = useState(goal.complete === "Y");

  const handleCheck = async (e) => {
    const res = await updateData(`/goal/${goal.id}/complete`, null);
    console.log("res", res);
    completeGoal(index, res);
    setIsCompleted(e.target.checked);
  }

  const removeGoal = async () => {
    await deleteData(`/goal/${goal.id}`);
    deleteGoal(goal.id);
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
              ? <span><del className="text-dark">{goal.content}</del></span>
              : <span>{goal.content}</span>
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
      <GoalCardTag isCompleted={isCompleted} plantName={goal.plantName} plantId={goal.plantId}/>
    </div>
  )
}

export default GoalCard;
