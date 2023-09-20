const sidebar = (state = {}, { type, name, payload, ...rest }) => {
  switch (type) {
    case 'setSidebar':
      return {...rest};
    default:
      return state
  }
}

export default sidebar;
