import getWateringMsg from "../../utils/function/getWateringMsg";

const getPlantListForPlantTable = (plants) => {
  const plantListForPlantTable = plants.map((pl) => {

    const plant = pl.plant;
    const gardenDetail = pl.gardenDetail;
    const latestWateringDate = gardenDetail.latestWateringDate;

    return ({
      key: plant.plantId,
      id: plant.plantId,
      name: plant.name,
      species: plant.species,
      recentWateringPeriod: plant.recentWateringPeriod,
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
        latestWateringDate: latestWateringDate ? `${latestWateringDate.wateringDate}\n(${latestWateringDate.chemicalName})` : "",
      }
    })
  })

  return plantListForPlantTable
}

export default getPlantListForPlantTable;
