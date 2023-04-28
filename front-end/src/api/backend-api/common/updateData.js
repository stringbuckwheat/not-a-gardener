import authAxios from "src/api/interceptors";

/**
 * PUT 요청 API
 * @param url
 * @param data
 * @returns response.data
 */
const updateData = async (url, data) => {
    return (await authAxios.put(url, data)).data;
}

export default updateData;
