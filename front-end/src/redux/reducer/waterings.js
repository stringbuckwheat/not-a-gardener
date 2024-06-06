const waterings = (state = {}, {type, payload}) => {
  switch (type){
    case 'setTotalWaterings':
      return {...state, totalWaterings: payload}
    case 'addWatering' :
      return {...state, totalWaterings: state.totalWaterings++}
    case 'deleteWatering':
      return {...state, totalWaterings: state.totalWaterings--}
    case 'deleteAllWaterings':
      return {...state, totalWaterings: 0}
    default:
      return state;
  }
}

export default waterings;
