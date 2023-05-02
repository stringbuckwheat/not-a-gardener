import {Space, Tooltip} from "antd";
import {QuestionCircleTwoTone} from "@ant-design/icons";

const getPlantListForPlacePlantTable = (plantList) => {
  return plantList.map(
    (rowData) => ({
      key: rowData.id,
      id: rowData.id,
      name: rowData.name,
      species: rowData.species,
      recentWateringPeriod:
        rowData.recentWateringPeriod == 0
          ? <Space align="middle"><Tooltip title={"물주기를 알아가는 중이에요"}><QuestionCircleTwoTone/></Tooltip></Space>
          : rowData.recentWateringPeriod,
      tags: [rowData.medium],
      createDate: rowData.createDate
    })
  );
}

export default getPlantListForPlacePlantTable;
