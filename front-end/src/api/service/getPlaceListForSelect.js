import authAxios from "src/utils/interceptors";

const getPlaceListForSelect = async (url) => {
    const placeList = (await authAxios.get(url)).data;

    return (
        placeList.map((place) => ({
            key: place.placeNo,
            value: place.placeName
        }))
    )
}

export default getPlaceListForSelect;