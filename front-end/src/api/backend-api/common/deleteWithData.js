import authAxios from "../../interceptors";

const deleteWithData = async (url, data) => {
  return (await authAxios.delete(url, {data: data})).data;
}

export default deleteWithData
