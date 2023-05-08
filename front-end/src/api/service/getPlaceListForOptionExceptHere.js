/**
 * 현재 장소(placeId)를 제외한 장소 목록을 반환
 * @param data
 * @param placeNo
 * @returns {*}
 */
const getPlaceListForOptionExceptHere = (rawPlaces, placeId) => {
  return rawPlaces.filter((place) => place.key != placeId)
    .map((place) => (
        {
          label: place.value,
          value: place.key
        }
      ))
}

export default getPlaceListForOptionExceptHere;
