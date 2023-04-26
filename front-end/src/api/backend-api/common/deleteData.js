import authAxios from "src/api/interceptors";

/**
 *
 * @param {} url
 * @param {} path
 */
const deleteData = (url, path) => {
    return authAxios.delete(`${url}/${path}`);
}

export default deleteData;
