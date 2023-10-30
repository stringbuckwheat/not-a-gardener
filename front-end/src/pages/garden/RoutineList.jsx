import {Link, useNavigate} from "react-router-dom";
import ClickableTag from "../../components/tag/basic/ClickableTag";
import {CCol} from "@coreui/react";
import React, {useState} from "react";
import RoutineStateUpdateModal from "../../components/modal/RoutineStateUpdateModal";
import {Tag} from "antd";

const RoutineList = ({routineList, afterRoutine}) => {
  const [isTitleHovered, setIsTitleHovered] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [routineForModal, setRoutineForModal] = useState({});

  const onClickTag = (routineForModal) => {
    console.log("onClickTag");
    setRoutineForModal(() => routineForModal);
    console.log('routineForModal', routineForModal);
    setIsModalVisible(true);
  }

  const navigate = useNavigate();
  const [isTagHovered, setIsTagHovered] = useState();

  return (
    <CCol md={6} xs={12}>
      <Link to="/schedule"
            className={`no-text-decoration text-${isTitleHovered ? "orange" : "black"}`}>
        <div
          className={`small fw-bold`}
          onMouseEnter={() => setIsTitleHovered(!isTitleHovered)}
          onMouseLeave={() => setIsTitleHovered(!isTitleHovered)}>
          {localStorage.getItem("name")}님의 루틴
        </div>
      </Link>
      <RoutineStateUpdateModal
        routineForModal={routineForModal}
        visible={isModalVisible}
        afterRoutine={afterRoutine}
        closeModal={() => setIsModalVisible(false)}/>
      <div>
        {
          routineList.length == 0
            ?
            <Tag
              color={isTagHovered ? "gold-inverse" : "gold"}
              onClick={() => navigate("/schedule")}
              onMouseEnter={() => setIsTagHovered(!isTagHovered)}
              onMouseLeave={() => setIsTagHovered(!isTagHovered)}
            >
              {isTagHovered ? " 첫 루틴을 등록할래요 " : "등록된 루틴이 없어요"}
            </Tag>
            :
            routineList.map((routine, index) => {
              const isCompleted = routine.isCompleted === "Y";
              const routineForModal = {routineId: routine.id, isCompleted, content: routine.content, index}

              return <ClickableTag
                key={routine.id}
                color={isCompleted ? "gold" : "geekblue"}
                onClick={() => onClickTag(routineForModal)}
                content={routine.content}/>
            })
        }
      </div>
    </CCol>
  )
}

export default RoutineList;
