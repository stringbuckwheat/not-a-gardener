import authAxios from "src/utils/interceptors";

const getFertilizerList = () => {
    return authAxios.get("/fertilizer")
}

export default getFertilizerList;