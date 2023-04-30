import React, {useState} from "react";
import CIcon from "@coreui/icons-react";
import {cilX} from "@coreui/icons";
import InputWithFeedback from "../../../components/input/InputWithFeedback";
import SelectPlant from "../../../components/select/SelectPlant";
import {Card} from "antd";
import ValidationSubmitButton from "../../../components/button/ValidationSubmitButton";
import InputFeedbackSpan from "../../../components/etc/InputFeedbackSpan";
import postData from "../../../api/backend-api/common/postData";

const AddGoal = ({onClickGoalFormButton, addGoal, plantList}) => {
  const style = {fontSize: "0.9em"}
  const formLabelColor = "garden"

  const [goal, setGoal] = useState({
    goalContent: "",
    plantId: 0,
  })

  const submit = async () => {
    const data = {...goal, complete: "N"};
    const res = await postData("/goal", data);
    addGoal(res);
    onClickGoalFormButton();
  }

  return (
    <Card className="mb-2">
      <div className="mb-2">
        <span className="fs-6 text-orange">목표 추가</span>
        <CIcon
          icon={cilX}
          className="float-end"
          onClick={onClickGoalFormButton}/>
      </div>
      <div className="mb-2">
        <InputWithFeedback
          label="목표"
          labelColor={formLabelColor}
          size="small"
          onChange={(e) => setGoal(() => ({...goal, goalContent: e.target.value}))}
          feedbackMsg={goal.goalContent == "" ? "목표를 입력해주세요" : ""}/>
      </div>
      <div className="mb-2">
        <span className={`text-${formLabelColor}`} style={style}>식물</span>
        <SelectPlant
          plantList={plantList}
          onChange={(plantId) => setGoal(() => ({...goal, plantId: plantId}))}
          size="small"
          className="width-full"/>
        <InputFeedbackSpan feedbackMsg="비워둬도 괜찮아요" color="success"/>
      </div>
      <ValidationSubmitButton
        isValid={goal.goalContent !== ""}
        onClickValid={submit}
        onClickInvalidMsg="입력 값을 확인해주세요"
        title="추가"
        className="float-end"
        size="small"
      />
    </Card>
  )
}

export default AddGoal
