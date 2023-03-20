import authAxios from "src/utils/interceptors";

const modifyFertilizer = (fertilizer) => {
    return authAxios.put("/fertilizer", fertilizer);
}

export default modifyFertilizer