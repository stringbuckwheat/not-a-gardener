import authAxios from "../interceptors";

/**
 * 장소 목록을 받아온 뒤 <Select> 용으로 가공
 * @returns {Promise<*>}
 */
const getPlaceList = async () => {
  const data = (await authAxios.get("/place")).data;

  const placeList = data.map((place) => (
    {
      key: place.id,
      value: place.name
    }
  ))

  return placeList;
}

export default getPlaceList;
