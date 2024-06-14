const initialState = {
  sidebarCollapsed: false,
  name: ""
}
const sidebar = (state = initialState, {type, payload}) => {
  switch (type) {
    // case 'setSidebar':
    case 'SET_SIDEBAR':
      return {...state, sidebarCollapsed: payload};
    // case 'setName':
    case 'SET_NAME':
      return {...state, name: payload};
    default:
      return state
  }
}

export default sidebar;
