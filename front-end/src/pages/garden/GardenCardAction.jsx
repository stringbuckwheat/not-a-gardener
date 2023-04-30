import GardenCardAnimatedButton from "../../components/animation/GardenCardAnimatedButton";
import React, {useState} from "react";
import {Button, Select, Space} from "antd";
import CIcon from "@coreui/icons-react";
import {cilDrop} from "@coreui/icons";
import postData from "../../api/backend-api/common/postData";
import getWateringNotificationMsg from "../../utils/function/getWateringNotificationMsg";

const GardenCardAction = ({
                            hovered,
                            plantId,
                            plantName,
                            chemicalList,
                            openNotification,
                            updateGardenAfterWatering,
                            y = 5,
                            wateringCode,
                            index,
                            deleteInTodoList,
                            postponeWatering
                          }) => {
  const [selected, setSelected] = useState("");
  const [chemicalId, setChemicalId] = useState(0);

  const submitWatering = async () => {
    const res = await postData(`/garden/${plantId}/watering`, {
      plantId,
      chemicalId,
      wateringDate: new Date().toISOString().split('T')[0]
    });

    // 메시지 띄우기
    const msg = getWateringNotificationMsg(res.wateringMsg.afterWateringCode);
    openNotification(msg);

    // garden 카드 갈아끼우기
    updateGardenAfterWatering(res.gardenResponse);

    setSelected("");
  }

  if (hovered && selected == "") {
    return <GardenCardAnimatedButton
      y={y}
      wateringCode={wateringCode}
      postponeWatering={postponeWatering}
      plantId={plantId}
      plantName={plantName}
      chemicalList={chemicalList}
      setSelected={setSelected}
      openNotification={openNotification}
      index={index}
      deleteInTodoList={deleteInTodoList}
    />
  } else if (selected === "watered") {
    return <>
      <div className="d-flex justify-content-between">
        <Space className="mb-1">
          <CIcon icon={cilDrop} className="text-info"/>
          <Select options={chemicalList} defaultValue={0} style={{width: 120}}
                  onChange={(value) => setChemicalId(value)}/>
          <span>을 줬어요</span>
        </Space>
        <Space>
          <Button onClick={submitWatering} className="bg-info text-white" size="small">제출</Button>
        </Space>
      </div>
    </>
  }
}

export default GardenCardAction;
