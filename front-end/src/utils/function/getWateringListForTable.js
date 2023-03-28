const getWateringListForTable = (wateringList) => {
  wateringList.map((watering) => (
    {
      ...watering,
      wateringPeriod: watering.wateringPeriod === 0
        ? ""
        : `${watering.wateringPeriod}일만에`
    }
  ))

  return wateringList;
}

export default getWateringListForTable;
