import getWateringMsg from "../../utils/function/getWateringMsg";

const getPlantListForPlantTable = (plantList) => {
  const plantListForPlantTable = plantList.map((pl) => {

    const plant = pl.plant;
    const gardenDetail = pl.gardenDetail;
    const latestWateringDate = gardenDetail.latestWateringDate;

    return ({
      key: plant.plantNo,
      plantNo: plant.plantNo,
      plantName: plant.plantName,
      plantSpecies: plant.plantSpecies,
      averageWateringPeriod: plant.averageWateringPeriod,
      earlyWateringPeriod: plant.earlyWateringPeriod,
      createDate: plant.createDate,

      placeName: plant.placeName,
      placeNo: plant.placeNo,

      chemicalCode: gardenDetail.chemicalCode,
      wateringCode: gardenDetail.wateringCode,
      wateringDDay: gardenDetail.wateringDDay,
      tags: {
        medium: plant.medium,
        wateringMsg: getWateringMsg(gardenDetail),
        anniversary: plant.birthday ? `${gardenDetail.anniversary} (${plant.birthday}~)` : null,
        latestWateringDate: latestWateringDate ? `${latestWateringDate.wateringDate}\n(${latestWateringDate.chemicalName})` : "",
      }
    })
  })

  return plantListForPlantTable
}

export default getPlantListForPlantTable;
