const getWateringListForTable = (wateringList) => {
  return wateringList.map((watering) => {
    return (
      {
        ...watering,
        period: watering.period == 0
          ? "첫 관수 기록일!"
          : `${watering.period}일만에`
      }
    )
  }
)

  return wateringList;
}

export default getWateringListForTable;
