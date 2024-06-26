const PlantDetailAction = Object.freeze({
  // PLANT_DETAIL
  FETCH_PLANT_DETAIL: "PLANT_DETAIL/FETCH",

  // PLACES
  SET_PLACES_FOR_SELECT: "PLANT_DETAIL/PLACES",

  // CHEMICALS
  SET_CHEMICALS_FOR_SELECT: "PLANT_DETAIL/CHEMICALS",

  // WATERINGS
  FETCH_WATERING: "PLANT_DETAIL/WATERING/FETCH",
  ADD_WATERING: "PLANT_DETAIL/WATERING/ADD",
  DELETE_WATERING: "PLANT_DETAIL/WATERING/DELETE",
  SET_WATERING_PAGE: "PLANT_DETAIL/WATERING/PAGE",

  // STATUS CHECK
  SET_HEAVY_DRINKER_CHECK: "PLANT_DETAIL/STATUS/HEAVY_DRINKER/SET",
  DELETE_HEAVY_DRINKER_CHECK: "PLANT_DETAIL/STATUS/HEAVY_DRINKER/DELETE",

  // ACTIVE STATUS
  FETCH_ACTIVE_STATUS: "PLANT_DETAIL/STATUS/FETCH",
  ADD_ACTIVE_STATUS: "PLANT_DETAIL/STATUS/ADD",
  DELETE_ACTIVE_STATUS: "PLANT_DETAIL/STATUS/DELETE",

  // STATUS LOG
  FETCH_STATUS_LOG: "PLANT_DETAIL/STATUS_LOG/FETCH",
  ADD_STATUS_LOG: "PLANT_DETAIL/STATUS_LOG/ADD",
  DELETE_STATUS_LOG_ONE: "PLANT_DETAIL/STATUS_LOG/DELETE",

  // REPOT
  FETCH_REPOT: "PLANT_DETAIL/REPOT/FETCH",
  ADD_REPOT: "PLANT_DETAIL/REPOT/ADD",
  DELETE_REPOT: "PLANT_DETAIL/REPOT/DELETE",

  // ETC
  SET_WATERING_FORM_OPENED: "PLANT_DETAIL/ETC/WATERING_FORM/OPENED",
  SET_EDITING_KEY: "PLANT_DETAIL/ETC/EDITING_KEY",
});

export default PlantDetailAction
