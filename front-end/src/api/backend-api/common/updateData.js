import authAxios from "src/utils/interceptors";

const updateData = async (url, path, data) => {
    return (await authAxios.put(`${url}/${path}`, data)).data;
}

export default updateData;
