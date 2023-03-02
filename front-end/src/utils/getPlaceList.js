import authAxios from "./requestInterceptor";

const getPlaceList = async () => {

    const res = await authAxios.get("/place");
    console.log("res", res);

    const placeList = res.data.map((item) => {
       return {
            key: item.placeNo,
            value: item.placeName
        }
    })

    return placeList;
}

export default getPlaceList;