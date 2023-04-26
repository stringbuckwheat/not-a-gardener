import updateData from "./common/updateData";

const updateRoutineState = async (routineNo, updateState) => {
  let data = {routineNo: routineNo};

  if (updateState) {
    data = {
      routineNo: routineNo,
      lastCompleteDate: new Date().toISOString().split("T")[0]
    };
  }

  return await updateData(`/routine/${routineNo}`, "complete", data);
}

export default updateRoutineState;
