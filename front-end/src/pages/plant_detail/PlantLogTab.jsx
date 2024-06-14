import {ConfigProvider, Tabs} from "antd";
import WateringList from "./watering/WateringList";
import themeGreen from "../../theme/themeGreen";
import PlantStatusLog from "./status/PlantStatusLog";
import RepotLog from "./repot/RepotLog";

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
      children: <PlantStatusLog/>
    },
    {
      key: "repot",
      label: "분갈이",
      children: <RepotLog/>
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
