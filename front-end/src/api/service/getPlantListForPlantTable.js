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

      fertilizingCode: gardenDetail.fertilizingCode,
      wateringCode: gardenDetail.wateringCode,
      wateringDDay: gardenDetail.wateringDDay,
      tags: {
        medium: plant.medium,
        anniversary: plant.birthday ? `${gardenDetail.anniversary} (${plant.birthday}~)` : null,
        latestWateringDate: latestWateringDate ? `${latestWateringDate.wateringDate}\n(${latestWateringDate.chemicalName})` : "",
      }
    })
  })

  return plantListForPlantTable
}

export default getPlantListForPlantTable;
