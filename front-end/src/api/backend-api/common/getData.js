import authAxios from "src/api/interceptors";

/**
 * GET 요청 API
 * @param url
 * @returns response의 data만 전달
 */
const getData = async (url) => {
    return (await authAxios.get(url)).data;
}

export default getData;
