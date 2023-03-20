const { default: authAxios } = require("src/utils/interceptors")

const addFertilizer = (fertilizer) => {
    return authAxios.post("/fertilizer", fertilizer);
}

export default addFertilizer;