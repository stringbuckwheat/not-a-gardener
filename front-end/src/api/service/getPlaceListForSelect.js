import authAxios from "src/api/interceptors";

const getPlaceListForSelect = async () => {
    const placeList = (await authAxios.get("/place")).data;

    return (
        placeList.map((place) => ({
            key: place.placeNo,
            value: place.placeName
        }))
    )
}

export default getPlaceListForSelect;
