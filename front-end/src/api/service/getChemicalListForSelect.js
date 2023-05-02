import authAxios from "../interceptors";

/**
 * 비료/살충/살균제 목록을 받아온 뒤 '맹물'을 넣어 가공한 뒤 반환
 * @param setChemicalList
 * @returns {Promise<void>}
 */
const getChemicalListForSelect = async (setChemicalList) => {
  const data = (await authAxios.get("/chemical")).data;
  console.log("data", data);

    // 맨 앞에 맹물 추가
    data.unshift({
        id: 0,
        name: '맹물'
    })

    // select 요구 사항에 맞게 배열 가공
    setChemicalList(data.map((chemical) => ({
        value: chemical.id,
        label: chemical.name
    })))
}

export default getChemicalListForSelect;
