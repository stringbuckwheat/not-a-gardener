import {Space, Tooltip} from "antd";
import {QuestionCircleOutlined, QuestionCircleTwoTone} from "@ant-design/icons";

const getPlantListForPlacePlantTable = (plantList) => {
  return plantList.map(
    (rowData) => ({
      key: rowData.plantNo,
      plantNo: rowData.plantNo,
      plantName: rowData.plantName,
      plantSpecies: rowData.plantSpecies,
      averageWateringPeriod:
        rowData.averageWateringPeriod == 0 ?
          <Space align="middle"><Tooltip
            title={"물주기를 알아가는 중이에요"}><QuestionCircleTwoTone/></Tooltip></Space> : rowData.averageWateringPeriod,
      tags: [rowData.medium],
      createDate: rowData.createDate
    })
  );
}

export default getPlantListForPlacePlantTable;
