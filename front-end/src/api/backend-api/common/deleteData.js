import authAxios from "src/utils/interceptors";

/**
 * 
 * @param {} url 
 * @param {} path 
 */
const deleteData = (url, path) => {
    authAxios.delete(`${url}/${path}`)
}

export default deleteData;