import authAxios from "src/api/interceptors";

const getGarden = async () => {
    const gardenList = (await authAxios.get("/garden")).data;

    // 정렬해서 보냄
    return gardenList.sort((a, b) => {
        return a.gardenDetail.wateringCode - b.gardenDetail.wateringCode;
    })
}

export default getGarden;
