import updateData from "./common/updateData";

/**
 * 루틴 완료/완료 취소 요청 API
 * @param routineId 필수
 * @param updateState 완료 요청 시 true, 완료 취소 요청 시 null
 * @returns {Promise<response.data>} update 후 response의 data
 */
const updateRoutineState = async (routineId, updateState) => {
  let data = {routineId};

  if (updateState) {
    data["lastCompleteDate"] = new Date().toISOString().split("T")[0];
  }

  return updateData(`/routine/${routineId}/complete`, data);
}

export default updateRoutineState;
