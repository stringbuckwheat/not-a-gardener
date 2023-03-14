import getFertilizerList from "../backend-api/fertilizer/getFertilizerList"

const getFertilizerListForAddWatering = async () => {
    const rawFertilizerList = await getFertilizerList();

    // key, label 모양의 객체 배열 리턴
    return rawFertilizerList.map((fertilizer) => (
        {
          key: fertilizer.fertilizerNo,
          label: fertilizer.fertilizerName
        }
      ));
}

export default getFertilizerListForAddWatering;