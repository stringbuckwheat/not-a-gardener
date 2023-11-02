const sidebar = (state = {}, { type, name, payload, ...rest }) => {
  switch (type) {
    case 'setSidebar':
      return {...state, ...rest};
    case 'setName':
      return {...state, name};
    default:
      return state
  }
}

export default sidebar;
