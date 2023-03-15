import authAxios from "src/utils/interceptors";

const onMount = async (url, setter) => {
    const data = (await authAxios.get(url)).data;
    setter(data);
}

export default onMount;