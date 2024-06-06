import NoContent from "../../../components/empty/NoContent";
import {Card} from "antd";
import React, {useState} from "react";
import AddRepot from "./AddRepot";
import RepotLog from "./RepotLog";

const Repot = ({repots}) => {

  const [isRepotFormOpened, setIsRepotFormOpened] = useState(false);
  const onClickRepotFormButton = () => {
    setIsRepotFormOpened(!isRepotFormOpened)
    console.log("onClickRepotFormButton", isRepotFormOpened);
  }

  return (
    <Card className="width-full" style={{margin: "0 0.5rem", padding: "0.5rem"}}>
      {
        repots.length == 0
          ?
          <NoContent
            isAddFormOpened={isRepotFormOpened}
            title="분갈이 기록"
            onClickShowAddForm={onClickRepotFormButton}
          >
            <AddRepot
              onClickRepotFormButton={onClickRepotFormButton}>
              <div>추가 폼</div>
            </AddRepot>
          </NoContent>
          :
          <RepotLog
            repots={repots}
            isRepotFormOpened={isRepotFormOpened}
            onClickRepotFormButton={onClickRepotFormButton}
          />
      }
    </Card>
  )
}

export default Repot
