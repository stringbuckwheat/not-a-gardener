import PlantStatusCode from "../../utils/code/plantStatusCode";

const initState = {
  detail: {},
  places: [],
  waterings: [],
  chemicals: [],
  etc: {
    wateringFormOpen: false,
    heavyDrinkerCheck: false,
    wateringPage: 1,
    editingKey: 0
  }
}

const getChemicalListForSelect = (payload) => {
  // 맨 앞에 맹물 추가
  payload.unshift({
    id: 0,
    name: '맹물'
  })

  return payload.map((chemical) => ({
    value: chemical.id,
    label: chemical.name
  }));
}

const plantDetail = (state = initState, {type, payload}) => {
  switch (type) {
    case 'setPlantDetail':
      return {...state, detail: payload};

    case 'setPlaceListForSelect':
      return {...state, places: payload};

    case 'setWateringsForPlantDetail':
      return {...state, waterings: payload};

    case 'setWateringPage':
      return {...state, etc: {...state.etc, wateringPage: payload}};

    case 'addWateringForPlantDetail':
      return {...state, waterings: {...state.waterings, payload}};

    case 'setChemicalsForSelect':
      return {...state, chemicals: getChemicalListForSelect(payload)};

    case 'setHeavyDrinkerCheck':
      return {...state, etc: {...state.etc, heavyDrinkerCheck: payload}};

    case 'deletePlantDetailWatering':
      const deletedWaterings = state.waterings.filter(w => w.id !== payload)
      return {...state, waterings: deletedWaterings};

    case 'deleteHeavyDrinkerStatus':
      const deletedStatus = state.detail.status?.filter(s => s.status !== PlantStatusCode.HEAVY_DRINKER.code);
      return {...state, detail: {...state.detail, status: deletedStatus}};

    case 'addStatus':
      const status = state.detail.status ?? [];
      return {...state, detail: {...state.detail, status: [...status, payload]}};

    case 'setWateringFormOpen':
      return {...state, etc: {...state.etc, wateringFormOpen: payload}};

    case 'setEditingKey':
      return {...state, etc: {...state.etc, editingKey: payload}};

    case 'setWateringPage':
      return {...state, etc: {...state.etc, wateringPage: payload}};

    default:
      return state;
  }
}

export default plantDetail;
