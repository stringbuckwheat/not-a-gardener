import authAxios from "src/api/interceptors";

/**
 *
 * @param {} url
 * @param {} path
 */
const deleteData = async (url) => {
    return (await authAxios.delete(url));
}

export default deleteData;
