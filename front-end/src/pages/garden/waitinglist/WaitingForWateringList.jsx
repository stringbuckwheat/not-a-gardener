import {CCol} from "@coreui/react";
import ClickableTag from "../../../components/tag/basic/ClickableTag";
import {Tooltip} from "antd";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";

const WaitingForWateringList = () => {
  const navigate = useNavigate();
  const waitingList = useSelector(state => state.gardens.waitingList);

  if(waitingList.length == 0){
    return <></>
  }

  if (waitingList.length == 0) {
    return;
  }

  return (
    <CCol md={6} xs={12}>
      <Tooltip placement={"topLeft"} title={"물 주기를 2회 이상 기록해주세요"}>
        <div className="small fw-bold text-black">물주기 정보를 기다리고 있는 식물들</div>
      </Tooltip>
      <div>
        {
          waitingList.map((plant, index) => (
            <ClickableTag
              color="green"
              content={plant.name}
              onClick={() => navigate(`/plant/${plant.id}`)}
              key={index}/>
          ))
        }
      </div>
    </CCol>
  )
}

export default WaitingForWateringList;
