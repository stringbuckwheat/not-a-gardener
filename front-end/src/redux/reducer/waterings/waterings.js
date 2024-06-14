import WateringAction from "./wateringAction";

const waterings = (state = {}, {type, payload}) => {
  switch (type){
    // case 'setTotalWaterings':
    case WateringAction.FETCH_TOTAL_WATERING:
      return {...state, totalWaterings: payload}
    // case 'addWatering' :
    case WateringAction.ADD_TOTAL_WATERING:
      return {...state, totalWaterings: state.totalWaterings++}
    // case 'deleteWatering':
    case WateringAction.DELETE_ONE_IN_TOTAL_WATERING:
      return {...state, totalWaterings: state.totalWaterings--}
    // case 'deleteAllWaterings':
    case WateringAction.DELETE_ALL_WATERING:
      return {...state, totalWaterings: 0}
    default:
      return state;
  }
}

export default waterings;
