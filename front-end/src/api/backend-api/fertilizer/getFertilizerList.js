import authAxios from "src/utils/interceptors";

const getFertilizerList = async () => {
     return (await authAxios.get("/fertilizer")).data;
}

export default getFertilizerList;