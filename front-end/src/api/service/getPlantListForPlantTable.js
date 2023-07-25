import getWateringMsg from "../../utils/function/getWateringMsg";
import {Space, Tooltip} from "antd";
import {QuestionCircleTwoTone} from "@ant-design/icons";

const getPlantListForPlantTable = (plants) => {
  const plantListForPlantTable = plants.map((pl) => {

    const plant = pl.plant;
    const gardenDetail = pl.gardenDetail;
    const latestWateringDate = gardenDetail.latestWateringDate;

    return ({
      key: plant.id,
      id: plant.id,
      name: plant.name,
      species: plant.species,
      recentWateringPeriod: plant.recentWateringPeriod == 0
        ? <Space align="middle"><Tooltip title={"물주기를 알아가는 중이에요"}><QuestionCircleTwoTone/></Tooltip></Space>
        : plant.recentWateringPeriod,
      earlyWateringPeriod: plant.earlyWateringPeriod,
      createDate: plant.createDate,

      placeName: plant.placeName,
      placeId: plant.placeId,

      chemicalCode: gardenDetail.chemicalCode,
      wateringCode: gardenDetail.wateringCode,
      wateringDDay: gardenDetail.wateringDDay,
      tags: {
        medium: plant.medium,
        wateringMsg: getWateringMsg(gardenDetail),
        anniversary: plant.birthday ? `${gardenDetail.anniversary} (${plant.birthday}~)` : null,
        latestWateringDate: latestWateringDate ? `${latestWateringDate.wateringDate}` : "",
      }
    })
  })

  return plantListForPlantTable
}

export default getPlantListForPlantTable;
