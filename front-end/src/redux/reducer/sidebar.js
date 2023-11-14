const initialState = {
  sidebarCollapsed: false,
  name: ""
}
const sidebar = (state = initialState, {type, payload}) => {
  switch (type) {
    case 'setSidebar':
      return {...state, sidebarCollapsed: payload};
    case 'setName':
      return {...state, name: payload};
    default:
      return state
  }
}

export default sidebar;
