import {CCol} from "@coreui/react";
import React, {useState} from "react";
import ClickableTag from "../../components/tag/basic/ClickableTag";
import WateredInGardenModal from "./WateredInGardenModal";

const WaitingForWateringList = ({waitingList, chemicalList, openNotification, updateWaitingListAfterWatering}) => {
  const [clickedPlant, setClickedPlant] = useState({});
  const onClick = (plant, index) => {
    setClickedPlant({...plant, index: index});
    setVisible(true);
  }

  // 물주기 모달
  const [visible, setVisible] = useState(false);
  const closeDeleteModal = () => {
    setVisible(false);
  }

  const afterFirstWatering = () => {
    updateWaitingListAfterWatering(clickedPlant.index);
    setVisible(false);
  }

  if(waitingList.length == 0){
    return
  }

  return (
    <CCol md={6} xs={12}>
      <div className="small fw-bold text-black">물주기 정보를 기다리고 있는 식물들</div>
      <WateredInGardenModal
        visible={visible}
        closeDeleteModal={closeDeleteModal}
        clickedPlant={clickedPlant}
        chemicalList={chemicalList}
        openNotification={openNotification}
        afterFirstWatering={afterFirstWatering}/>
      <div>
        {
          waitingList.map((plant, index) => (
            <ClickableTag
              color="green"
              content={plant.plantName}
              onClick={() => onClick(plant, index)}
              key={index}/>
          ))
        }
      </div>
    </CCol>
  )
}

export default WaitingForWateringList;
