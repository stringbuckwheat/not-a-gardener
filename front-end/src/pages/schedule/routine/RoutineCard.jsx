import {Checkbox, Popconfirm, Space, Tag} from "antd";
import {useNavigate} from "react-router-dom";
import {CloseOutlined} from "@ant-design/icons";
import deleteData from "../../../api/backend-api/common/deleteData";
import updateData from "../../../api/backend-api/common/updateData";

const RoutineCard = ({routine, deleteRoutine, index, completeRoutine}) => {
  const navigate = useNavigate();

  const onChange = async (e) => {
    console.log(`checked = ${e.target.checked}`);
    console.log("e.target", e);

    if (e.target.checked) {
      const data = {routineNo: routine.routineNo, lastCompleteDate: new Date().toISOString().split("T")[0]};
      console.log("complete data", data);
      const res = await updateData(`/routine/${routine.routineNo}/complete`, "", data);
      console.log("complete res", res);
      completeRoutine(index, res);
      e.target.checked = false;
    }
  };

  const removeRoutine = (routineNo, hasToDoToday) => {
    deleteData("/routine", routineNo);
    deleteRoutine(routineNo, hasToDoToday);
  }

  const isCompleted = routine.isCompleted == "Y" ? true : false;
  const hasToDoToday = routine.hasToDoToday == "Y" ? true : false;

  return (
    <div className="mb-3">
      <div>
        <Space>
          <Checkbox
            onChange={onChange}
            defaultChecked={routine.isCompleted == "Y" ? true : false}
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
