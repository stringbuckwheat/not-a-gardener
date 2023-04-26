import authAxios from "src/api/interceptors";

/**
 * post 이후 response로 받은 데이터를 돌려준다
 * @param url PostMapping
 * @param data 제출할 데이터
 * @returns {Promise<any>} res.data
 */
const postData = async (url, data) => {
    return (await authAxios.post(url, data)).data;
}

export default postData;
