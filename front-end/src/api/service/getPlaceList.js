import authAxios from "../interceptors";

const getPlaceList = async () => {
    const res = await authAxios.get("/place");

    const placeList = res.data.map((item) => {
       return {
            key: item.placeNo,
            value: item.placeName
        }
    })

    return placeList;
}

export default getPlaceList;
