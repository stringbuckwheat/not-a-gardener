import getData from "../backend-api/common/getData";

const getChemicalListForSelect = async (setChemicalList) => {
    const data = await getData("/chemical");

    // 맨 앞에 맹물 추가
    data.unshift({
        chemicalNo: 0,
        chemicalName: '맹물'
    })

    // select 요구 사항에 맞게 배열 가공
    setChemicalList(data.map((chemical) => ({
        value: chemical.chemicalNo,
        label: chemical.chemicalName
    })))
}

export default getChemicalListForSelect;