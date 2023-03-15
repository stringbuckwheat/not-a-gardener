import authAxios from "src/utils/interceptors";

const getData = async (url) => {
    return (await authAxios.get(url)).data;
}

export default getData;