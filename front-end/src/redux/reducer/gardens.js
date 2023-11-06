const initialState = {
  todoList: [],
  waitingList: [],
  routineList: []
}

const gardens = (state = initialState, {type, payload}) => {
  switch(type){
    case 'setTodoList':
      return {...state, todoList: payload}
    case 'setWaitingList':
      return {...state, waitingList: payload}
    case 'setRoutineList':
      return {...state, routineList: payload}
    case 'updateRoutine':
      const routines = state.routineList.filter((routine) => routine.id !== payload.id)
      return {...state, routineList: [...routines, payload]}
    case 'updateTodoList':
      const prevGarden = state.todoList.splice(payload.index, 1)[0];
      const prevGardenDetail = prevGarden.gardenDetail;
      const newGardenDetail = {...prevGardenDetail, wateringCode: payload.res};

      return {...state, todoList: [...state.todoList, {...prevGarden, gardenDetail: newGardenDetail}]}
    case 'deleteInWaitingList':
      const waitings = state.waitingList.filter((plant) => plant.id !== payload);
      return {...state, waitingList: waitings}
    case 'deleteInTodoList':
      console.log("--", state.todoList[0]);
      console.log("payload", payload);
      const todos = state.todoList.filter((plant) => plant.plant.id !== payload);
      return {...state, todoList: todos};
    default:
      return state;
  }
}

export default gardens;
