import {ConfigProvider, Tabs} from "antd";
import WateringList from "./watering/WateringList";
import themeGreen from "../../../theme/themeGreen";
import PlantStatus from "./status/PlantStatus";

const PlantLogTab = ({wateringCallBack, openNotification}) => {
  const items = [
    {
      key: "watering",
      label: "물주기",
      children: <WateringList wateringCallBack={wateringCallBack} openNotification={openNotification}/>,
    },
    {
      key: "status",
      label: "식물 상태",
      children: <PlantStatus/>
    }
  ]

  return (
    <ConfigProvider theme={themeGreen}>
      <Tabs
        style={{marginTop: "1rem", width: "100%"}}
        defaultActiveKey="2"
        items={items}
      />
    </ConfigProvider>
  )
}

export default PlantLogTab;
