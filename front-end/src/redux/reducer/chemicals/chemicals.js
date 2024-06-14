import ChemicalAction from "./chemicalAction";

const initialState = {
  chemicals: [],
  forSelect: [],
}

const getChemicalsForSelect = (rawChemicals) => {
  const copy = [...rawChemicals];
  // 맨 앞에 맹물 추가
  copy.unshift({
    id: 0,
    name: '맹물'
  })

  return copy.map((chemical) => ({
    value: chemical.id,
    label: chemical.name
  }))
}

const chemicals = (state = initialState, {type, payload}) => {
  switch (type) {
    case ChemicalAction.FETCH_CHEMICAL:
      return {...state, chemicals: payload, forSelect: getChemicalsForSelect(payload)};

    case ChemicalAction.ADD_CHEMICAL:
      return {...state, chemicals: state.chemicals.concat(payload)};

    default:
      return state;
  }
}

export default chemicals;
