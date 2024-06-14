import PlantAction from "./plantAction";

const plants = (state = [], {type, payload}) => {
  switch (type) {
    // case 'setPlants':
    case PlantAction.FETCH_PLANT:
      return [...payload];

    // case 'addPlant':
    case PlantAction.ADD_PLANT:
      return [payload, ...state];

    // case 'updatePlant':
    case PlantAction.UPDATE_PLANT:
      const editedId = payload.plant.id;

      return state.map((plant) => {
        return plant.plant.id == editedId ? payload : plant;
      })

    case PlantAction.DELETE_PLANT:
      return state.filter(plant => plant.plant.id !== payload);

    default:
      return state;
  }
}

export default plants;
