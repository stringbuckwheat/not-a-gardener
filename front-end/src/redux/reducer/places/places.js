import {PlaceAction} from "./placeAction";

const initialState = {
  places: [],
  forSelect: []
}

const getPlacesForSelect = (rawPlaces) => {
  return rawPlaces.map((place) => ({
    value: place.id,
    label: place.name
  }))
}

const places = (state = initialState, {type, payload}) => {
  switch (type){
    // case 'setPlaces':
    case PlaceAction.FETCH_PLACES:
      return {...state, places: payload, forSelect: getPlacesForSelect(payload)}
    // case 'addPlace':
    case PlaceAction.ADD_PLACES:
      return {...state, places: state.places.concat(payload)};
    default:
      return state
  }
}

export default places;
