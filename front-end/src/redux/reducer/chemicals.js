const initialState = {
  chemicals: [],
  forSelect: [],
}

const getChemicalsForSelect = (rawChemicals) => {
  return rawChemicals.map((chemical) => ({
    value: chemical.id,
    label: chemical.name
  }))
}

const chemicals = (state = initialState, {type, payload}) => {
  switch (type) {
    case 'setChemicals':
      return {...state, chemicals: payload, forSelect: getChemicalsForSelect(payload)}

    case 'setForSelect':
      return {...state, forSelect: getChemicalsForSelect(payload)}

    default:
      return state
  }
}

export default chemicals;
