import authAxios from "src/utils/interceptors";

const getGarden = async () => {
    const gardenList = (await authAxios.get("/garden")).data;
    console.log("gardenList", gardenList);

    // 정렬해서 보냄
    return gardenList.sort((a, b) => {
        return a.wateringCode - b.wateringCode;
    })
}

export default getGarden;