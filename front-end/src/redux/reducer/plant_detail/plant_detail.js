import PlantStatusCode from "../../../utils/code/plantStatusCode";
import PlantDetailAction from "./plantDetailAction";

const initState = {
  detail: {
    status: {}
  },
  places: [],
  waterings: [],
  chemicals: [],
  statusLog: [],
  repot: [],
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
    case PlantDetailAction.FETCH_PLANT_DETAIL:
      return {...state, detail: payload};

    case PlantDetailAction.SET_PLACES_FOR_SELECT:
      return {...state, places: payload};

    case PlantDetailAction.FETCH_WATERING:
      return {...state, waterings: payload};

    case PlantDetailAction.ADD_WATERING:
      return {...state, waterings: {...state.waterings, payload}};

    case PlantDetailAction.DELETE_WATERING:
      const deletedWaterings = state.waterings.filter(w => w.id !== payload)
      return {...state, waterings: deletedWaterings};

    case PlantDetailAction.SET_WATERING_PAGE:
      return {...state, etc: {...state.etc, wateringPage: payload}};

    ///// CHEMICAL
    case PlantDetailAction.SET_CHEMICALS_FOR_SELECT:
      return {...state, chemicals: getChemicalListForSelect(payload)};

    ///// STATUS CHECK
    case PlantDetailAction.SET_HEAVY_DRINKER_CHECK:
      return {...state, etc: {...state.etc, heavyDrinkerCheck: payload}};

    case PlantDetailAction.DELETE_HEAVY_DRINKER_CHECK:
      const deletedStatus = state.detail.status?.filter(s => s.status !== PlantStatusCode.HEAVY_DRINKER.code);
      return {...state, detail: {...state.detail, status: deletedStatus}};

    ///// ACTIVE_STATUS
    case PlantDetailAction.FETCH_ACTIVE_STATUS:
      return {...state, detail: {...state.detail, status: payload}};

    case PlantDetailAction.ADD_ACTIVE_STATUS:
      console.log("ADD_ACTIVE_STATUS", {...state.detail.status, ...payload});
      return {...state, detail: {...state.detail, status: {...state.detail.status, ...payload}}};

    case PlantDetailAction.DELETE_ACTIVE_STATUS:
      console.log("DELETE_ACTIVE_STATUS", {...state.detail.status, [payload.status]: "N"})
      return {
        ...state,
        detail: {...state.detail, status: {...state.detail.status, [payload.status]: "N"}}
      }

    ///// STATUS_LOG
    case PlantDetailAction.FETCH_STATUS_LOG:
      return {...state, statusLog: payload};

    case PlantDetailAction.ADD_STATUS_LOG:
      const newStatusLog = [...state.statusLog, payload];

      newStatusLog.sort((a, b) => {
        // 먼저 recordedDate로 정렬
        if (new Date(b.recordedDate) - new Date(a.recordedDate) === 0) {
          // recordedDate가 같으면 createDate로 정렬
          return new Date(b.createDate) - new Date(a.createDate);
        }
        // recordedDate로 정렬
        return new Date(b.recordedDate) - new Date(a.recordedDate);
      });

      return {...state, statusLog: newStatusLog};

    case PlantDetailAction.DELETE_STATUS_LOG_ONE:
      return {...state, statusLog: state.statusLog.filter((s) => s.statusLogId != payload)}

    ///// REPOT
    case PlantDetailAction.FETCH_REPOT:
      return {...state, repot: payload};

    case PlantDetailAction.DELETE_REPOT:
      return {...state, repot: state.repot.filter((r) => r.repotId != payload)};

    case PlantDetailAction.ADD_REPOT:
      const newRepot = [payload, ...state.repot];

      newRepot.sort((a, b) => {
        // recordedDate로 정렬
        return new Date(b.repotDate) - new Date(a.repotDate);
      });

      return {...state, repot: newRepot};

    ///// ETC
    case PlantDetailAction.SET_WATERING_FORM_OPENED:
      return {...state, etc: {...state.etc, wateringFormOpen: payload}};

    case PlantDetailAction.SET_EDITING_KEY:
      return {...state, etc: {...state.etc, editingKey: payload}};

    default:
      return state;
  }
}

export default plantDetail;
