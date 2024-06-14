import GardenAction from "./gardenAction";

const initialState = {
  todoList: [],
  waitingList: [],
  routineList: [],
  attentions: []
}

const gardens = (state = initialState, {type, payload}) => {
  switch (type) {
    ///// ALL
    case GardenAction.FETCH_GARDEN:
      const {todoList, waitingList, routineList, attentions} = payload;
      return {...state, todoList, waitingList, routineList, attentions};

    ///// ATTENTIONS
    case GardenAction.ADD_ATTENTION:
      return {...state, attentions: [...state.attentions, payload]}


    ///// TODOLIST
    case GardenAction.FETCH_TODOLIST:
      return {...state, todoList: payload}

    case GardenAction.UPDATE_TODOLIST:
      const prevGarden = state.todoList.splice(payload.index, 1)[0];
      const prevGardenDetail = prevGarden.gardenDetail;
      const newGardenDetail = {...prevGardenDetail, wateringCode: payload.res};

      return {...state, todoList: [...state.todoList, {...prevGarden, gardenDetail: newGardenDetail}]}

    case GardenAction.DELETE_TODOLIST:
      const todos = state.todoList.filter((plant) => plant.plant.id !== payload);
      return {...state, todoList: todos};

    ///// WAITINGS
    case GardenAction.FETCH_WAITINGS:
      return {...state, waitingList: payload}

    case GardenAction.DELETE_WAITING:
      const waitings = state.waitingList.filter((plant) => plant.id !== payload);
      return {...state, waitingList: waitings}

    ///// ROUTINES
    case GardenAction.FETCH_ROUTINES:
      return {...state, routineList: payload}

    case GardenAction.UPDATE_ROUTINES:
      const routines = state.routineList.filter((routine) => routine.id !== payload.id)
      return {...state, routineList: [...routines, payload]}

    default:
      return state;
  }
}

export default gardens;
