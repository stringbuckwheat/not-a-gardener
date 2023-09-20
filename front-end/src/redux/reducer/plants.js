const plants = (state = [], {type, payload}) => {
  switch (type){
    case 'setPlants':
      return [...payload];
    case 'addPlant':
      return state.concat(payload);
    default:
      return state;
  }
}

export default plants;
