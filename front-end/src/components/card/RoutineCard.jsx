import {Checkbox, Popconfirm, Space, Tag} from "antd";
import {CloseOutlined} from "@ant-design/icons";
import deleteData from "../../api/backend-api/common/deleteData";
import {useState} from "react";
import LinkHoverTag from "../tag/basic/LinkHoverTag";
import updateRoutineState from "../../api/backend-api/updateRoutineState";

const RoutineCard = ({routine, deleteRoutine, index, completeRoutine}) => {
  const [isCompleted, setIsCompleted] = useState(routine.isCompleted == "Y");

  const hasToDoToday = routine.hasToDoToday == "Y";

  const onChange = async (e) => {
    const res = await updateRoutineState(routine.routineNo, e.target.checked);
    completeRoutine(index, res);
    setIsCompleted(e.target.checked);
  };

  const removeRoutine = async (routineNo, hasToDoToday) => {
    await deleteData(`/routine/${routineNo}`);
    deleteRoutine(routineNo, hasToDoToday);
  }

  return (
    <div className="mb-3">
      <div>
        <Space>
          <Checkbox
            onChange={onChange}
            defaultChecked={isCompleted}
            disabled={!hasToDoToday}
          />
          {
            isCompleted
              ? <span><del className="text-dark">{routine.routineContent}</del></span>
              : <span>{routine.routineContent}</span>
          }
        </Space>
        <Popconfirm
          title="이 루틴을 삭제할까요?"
          description="루틴을 삭제하면 복구할 수 없어요"
          onConfirm={() => removeRoutine(routine.routineNo, routine.hasToDoToday)}
          okText="네"
          cancelText="아니요"
        >
          <CloseOutlined className="m-1 float-end text-dark" style={{fontSize: "0.9em"}}/>
        </Popconfirm>

      </div>
      <div className="d-flex justify-content-end">
        <Tag color={`${isCompleted || !hasToDoToday ? "" : "blue"}`}>{`${routine.routineCycle}일마다 한 번`}</Tag>
        <LinkHoverTag content={routine.plantName} color={"green"} to={`/plant/${routine.plantNo}`} />
      </div>
    </div>
  )
}

export default RoutineCard
