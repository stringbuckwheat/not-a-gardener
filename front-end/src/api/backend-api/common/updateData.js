import authAxios from "src/api/interceptors";

const updateData = async (url, data) => {
    return (await authAxios.put(url, data)).data;
}

export default updateData;
