const getPlantListForPlacePlantTable = (plantList) => {
    return plantList.map(
        (rowData) => ({
            key: rowData.plantNo,
            plantNo: rowData.plantNo,
            plantName: rowData.plantName,
            plantSpecies: rowData.plantSpecies,
            averageWateringPeriod: rowData.averageWateringPeriod,
            tags: [rowData.medium],
            createDate: rowData.createDate
        })
    );
} 

export default getPlantListForPlacePlantTable;