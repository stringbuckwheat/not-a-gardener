import GButton from "../button/defaultButton/GButton";
import React from "react";

/**
 * 스케줄(목표, 루틴)의 data 없음 페이지
 * @param isAddFormOpened
 * @param title
 * @param onClickShowAddForm
 * @param children
 * @returns {JSX.Element}
 * @constructor
 */
const NoSchedule = ({isAddFormOpened, title, onClickShowAddForm, children}) => {
  const msg = title === "목표" ? `저장된 ${title}가 없어요.` : `저장된 ${title}이 없어요.`

  return (
    <div className="d-flex justify-content-center align-items-center height-70vh">
      <div>
        {isAddFormOpened
          ? <>{children}</>
          : <>
            <div className="fs-5 mb-2 text-garden">{msg}</div>
            <div className="text-center">
              <GButton color="garden" onClick={onClickShowAddForm}>{`${title} 추가`}</GButton>
            </div>
          </>}
      </div>
    </div>
  )
}

export default NoSchedule
