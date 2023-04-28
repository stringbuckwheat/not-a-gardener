import authAxios from "src/api/interceptors";

/**
 * 삭제 요청 api
 * return void
 * @param {} url
 */
const deleteData = async (url) => {
    return (await authAxios.delete(url));
}

export default deleteData;
