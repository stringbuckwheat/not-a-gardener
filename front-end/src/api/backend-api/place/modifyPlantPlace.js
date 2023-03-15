import authAxios from "src/utils/interceptors";

/**
 * 한 장소의 식물들 위치를 바꾼다
 * @param { placeNo: placeNo, plantList: props.selectedPlantNo } data 
 * return void
 */

const modifyPlantPlace = async (data) => {
    return (await authAxios.put("/plant/modify-place", data)).data;
}

export default modifyPlantPlace;