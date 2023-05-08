import {Link} from "react-router-dom";
import ClickableTag from "../../components/tag/basic/ClickableTag";
import {CCol} from "@coreui/react";
import React, {useState} from "react";
import RoutineStateUpdateModal from "../../components/modal/RoutineStateUpdateModal";
import ContentChangeWhenHoveredTag from "../../components/tag/basic/ContentChangeWhenHoveredTag";

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
            ? <ContentChangeWhenHoveredTag to={"/schedule"} color={"gold"} hoveredContent={" 첫 루틴을 등록할래요 "} defaultContent={"등록된 루틴이 없어요"} />
            :
            routineList.map((routine, index) => {
              const isCompleted = routine.isCompleted === "Y";
              const routineForModal = {routineId: routine.id, isCompleted, content: routine.content, index}
              // console.log("routineForModal", routineForModal);

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
