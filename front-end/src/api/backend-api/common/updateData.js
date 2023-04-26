import authAxios from "src/api/interceptors";

const updateData = async (url, path, data) => {
    return (await authAxios.put(`${url}/${path}`, data)).data;
}

export default updateData;
