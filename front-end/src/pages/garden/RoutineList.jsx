import {Link} from "react-router-dom";
import ClickableTag from "../../components/tag/basic/ClickableTag";
import {CCol} from "@coreui/react";
import React, {useState} from "react";
import RoutineStateUpdateModal from "../../components/modal/RoutineStateUpdateModal";

const RoutineList = ({routineList, afterRoutine}) => {
  const [isTitleHovered, setIsTitleHovered] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [routineForModal, setRoutineForModal] = useState({});

  const onClickTag = (routineNo, isCompleted, routineContent, index) => {
    setRoutineForModal({routineNo, isCompleted, routineContent, index});
    setIsModalVisible(true);
  }

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
          routineList.map((routine, index) => {
            const isCompleted = routine.isCompleted === "Y";

            return <ClickableTag
              key={routine.routineNo}
              color={isCompleted ? "yellow" : "geekblue"}
              onClick={() => onClickTag(routine.routineNo, isCompleted, routine.routineContent, index)}
              content={routine.routineContent}/>
          })
        }
      </div>
    </CCol>
  )
}

export default RoutineList;
