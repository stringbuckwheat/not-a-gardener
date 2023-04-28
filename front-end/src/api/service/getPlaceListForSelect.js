import authAxios from "src/api/interceptors";

/**
 * 장소 리스트를 <Select>에서 쓸 수 있도록 가공
 * @returns {Promise<*>} {key: placeId, value: 장소이름}으로 가공된 리스트
 */
const getPlaceListForSelect = async () => {
  const places = (await authAxios.get("/place")).data;

  return (
    places.map((place) => ({
      key: place.placeId,
      value: place.name
    }))
  )
}

export default getPlaceListForSelect;
