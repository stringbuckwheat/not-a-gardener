import {Link} from "react-router-dom";
import ClickableTag from "../../components/tag/basic/ClickableTag";
import {CCol} from "@coreui/react";
import React, {useState} from "react";

const RoutineList = ({routineList}) => {
  const [isTitleHovered, setIsTitleHovered] = useState(false);

  const onClick = (routineNo, isCompleted) => {
    if (!isCompleted) {
      //
    }
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
      <div>
        {
          routineList.map((routine) => {
            const isCompleted = routine.isCompleted === "Y";

            return <ClickableTag
              key={routine.routineNo}
              color={isCompleted ? "yellow" : "geekblue"}
              onClick={() => onClick(routine.routineNo, isCompleted)}
              content={routine.routineContent}/>
          })
        }
      </div>
    </CCol>
  )
}

export default RoutineList;
