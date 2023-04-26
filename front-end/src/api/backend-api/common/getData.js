import authAxios from "src/api/interceptors";

const getData = async (url) => {
    return (await authAxios.get(url)).data;
}

export default getData;
