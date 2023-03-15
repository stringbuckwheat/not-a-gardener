import authAxios from "src/utils/interceptors";

/**
 * db에 입력 후 response 돌려줌
 */
const insertData = async (url, data) => {
    return (await authAxios.post(url, data)).data;
}

export default insertData;