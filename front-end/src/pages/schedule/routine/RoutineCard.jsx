import {Checkbox, Popconfirm, Space, Tag} from "antd";
import {useNavigate} from "react-router-dom";
import {CloseOutlined} from "@ant-design/icons";
import deleteData from "../../../api/backend-api/common/deleteData";
import updateData from "../../../api/backend-api/common/updateData";
import {useState} from "react";

const RoutineCard = ({routine, deleteRoutine, index, completeRoutine}) => {
  const [isCompleted, setIsCompleted] = useState(routine.isCompleted == "Y");

  const navigate = useNavigate();
  const hasToDoToday = routine.hasToDoToday == "Y";

  const onChange = async (e) => {
    let data = {routineNo: routine.routineNo};

    if (e.target.checked) {
      data = {
        routineNo: routine.routineNo,
        lastCompleteDate: new Date().toISOString().split("T")[0]
      };
    }

    const res = await updateData(`/routine/${routine.routineNo}/complete`, "", data);
    completeRoutine(index, res);
    setIsCompleted(e.target.checked);
  };

  const removeRoutine = (routineNo, hasToDoToday) => {
    deleteData("/routine", routineNo);
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
        <Tag color={`${isCompleted || !hasToDoToday ? "" : "green"}`}
             onClick={() => navigate(`/plant/${routine.plantNo}`)}>{routine.plantName}</Tag>
        <Tag color={`${isCompleted || !hasToDoToday ? "" : "blue"}`}>{`${routine.routineCycle}일마다 한 번`}</Tag>
      </div>
    </div>
  )
}

export default RoutineCard
