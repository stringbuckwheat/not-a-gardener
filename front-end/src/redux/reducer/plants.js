const plants = (state = [], {type, payload}) => {
  switch (type) {
    case 'setPlants':
      return [...payload];
    case 'addPlant':
      return [payload, ...state];
    case 'updatePlant':
      const editedId = payload.plant.id;

      return state.map((plant) => {
        return plant.plant.id == editedId ? payload : plant;
      })
    case 'deletePlant':
      return state.filter(plant => plant.plant.id !== payload);
    default:
      return state;
  }
}

export default plants;
