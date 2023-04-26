import authAxios from "src/api/interceptors";

const onMountWithLength = async (url, setData, setDataLength) => {
    const data = (await authAxios.get(url)).data;

    setDataLength(data.length > 0);
    setData(data);
}

export default onMountWithLength
