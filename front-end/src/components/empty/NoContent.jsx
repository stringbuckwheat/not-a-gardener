import GButton from "../button/GButton";
import React from "react";
import Style from "./NoSchedule.module.scss"

/**
 * 스케줄(목표, 루틴)의 data 없음 페이지
 * @param isAddFormOpened
 * @param title
 * @param onClickShowAddForm
 * @param children
 * @returns {JSX.Element}
 * @constructor
 */
const NoContent = ({isAddFormOpened, title, onClickShowAddForm, children}) => {
  const msg = title === "목표" ? `저장된 ${title}가 없어요.` : `저장된 ${title}이 없어요.`

  return (
    <div className={Style.wrap}>
      <div>
        {isAddFormOpened
          ? <>{children}</>
          : <>
            <div className={Style.msg}>{msg}</div>
            <div className={Style.center}>
              <GButton onClick={onClickShowAddForm}>{`${title} 추가`}</GButton>
            </div>
          </>}
      </div>
    </div>
  )
}

export default NoContent
