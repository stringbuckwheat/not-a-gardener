import GardenCardAnimatedButton from "../../components/animation/GardenCardAnimatedButton";
import React, {useState} from "react";
import {Button, Select, Space} from "antd";
import CIcon from "@coreui/icons-react";
import {cilDrop} from "@coreui/icons";
import postData from "../../api/backend-api/common/postData";
import getWateringNotificationMsg from "../../utils/function/getWateringNotificationMsg";

const GardenCardAction = ({hovered, plantNo, plantName, chemicalList, openNotification, updateGardenAfterWatering}) => {
  const [selected, setSelected] = useState("");

  const [chemicalNo, setChemicalNo] = useState(0);

  const onClick = async () => {
    const data = {
      plantNo: plantNo,
      chemicalNo: chemicalNo,
      wateringDate: new Date().toISOString().split('T')[0]
    }

    const res = await postData("/garden/watering", data);
    console.log("res", res);

    // 메시지 띄우기
    const msg = getWateringNotificationMsg(res.wateringMsg.wateringCode);
    openNotification(msg);

    // garden 카드 갈아끼우기
    updateGardenAfterWatering(res.gardenResponse);

    setSelected("");
  }

  if (hovered && selected == "") {
    return <GardenCardAnimatedButton
      plantNo={plantNo}
      plantName={plantName}
      chemicalList={chemicalList}
      setSelected={setSelected}/>
  } else if (selected === "watered") {
    return <>
      <div className="d-flex justify-content-between">
        <Space className="mb-1">
          <CIcon icon={cilDrop} className="text-info"/>
          <Select options={chemicalList} defaultValue={0} style={{width: 120}} onChange={(value) => setChemicalNo(value)}/>
          <span>을 줬어요</span>
        </Space>
        <Space>
        <Button onClick={onClick} className="bg-info text-white" size="small">제출</Button>
        </Space>
      </div>
    </>
  } else if (selected === "notDry") {
    return <div>안 마름</div>
  } else if (selected === "lazy") {
    return <div>게으름</div>
  }
}

export default GardenCardAction;
