import {Link, useNavigate} from "react-router-dom";
import ClickableTag from "../../components/tag/basic/ClickableTag";
import React, {useState} from "react";
import RoutineStateUpdateModal from "../../components/modal/RoutineStateUpdateModal";
import {Col, Tag} from "antd";
import {useSelector} from "react-redux";

const RoutineList = () => {
  const [isTitleHovered, setIsTitleHovered] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [routineForModal, setRoutineForModal] = useState({});
  const [isTagHovered, setIsTagHovered] = useState();

  const routineList = useSelector(state => state.gardens.routineList);

  const onClickTag = (routineForModal) => {
    setRoutineForModal(() => routineForModal);
    setIsModalVisible(true);
  }

  const navigate = useNavigate();

  return (
    <Col md={12} xs={24}>
      <Link to="/schedule"
            className={`no-text-decoration text-${isTitleHovered ? "orange" : "black"}`}>
        <span
          className={`small fw-bold`}
          onMouseEnter={() => setIsTitleHovered(!isTitleHovered)}
          onMouseLeave={() => setIsTitleHovered(!isTitleHovered)}>
          {localStorage.getItem("name")}님의 루틴
        </span>
      </Link>
      <RoutineStateUpdateModal
        visible={isModalVisible}
        closeModal={() => setIsModalVisible(false)}
        routineForModal={routineForModal}
      />
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
    </Col>
  )
}

export default RoutineList;
