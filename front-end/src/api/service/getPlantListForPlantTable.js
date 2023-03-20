const getPlantListForPlantTable = (plantList) => {
    console.log("메소드 plantList", plantList);

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
            
            placeName: plant.placeName,
            placeNo: plant.placeNo,
            
            fertilizingCode: gardenDetail.fertilizingCode,
            wateringCode: gardenDetail.wateringCode,
            wateringDDay: gardenDetail.wateringDDay,
            latestWateringDate: latestWateringDate ? `${latestWateringDate.wateringDate} (${latestWateringDate.chemicalName})` : "",
            
            tags: {
                    medium: plant.medium,
                    createDate: `${plant.createDate}부터 기록 중`,
                    anniversary: plant.birthday ? `${gardenDetail.anniversary} (${plant.birthday}~)` : null,
                }
        })
    })

    return plantListForPlantTable
}

export default getPlantListForPlantTable;