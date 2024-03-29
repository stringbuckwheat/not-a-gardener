import React, {useState} from "react";
import InputWithFeedback from "../../../components/form/input/InputWithFeedback";
import SelectPlant from "../../../components/select/SelectPlant";
import {Card} from "antd";
import ValidationSubmitButton from "../../../components/button/ValidationSubmitButton";
import InputFeedbackSpan from "../../../components/etc/InputFeedbackSpan";
import postData from "../../../api/backend-api/common/postData";
import {CloseOutlined} from "@ant-design/icons";

const AddGoal = ({onClickGoalFormButton, addGoal}) => {
  const style = {fontSize: "0.9em"}
  const formLabelColor = "garden"

  const [goal, setGoal] = useState({
    content: "",
    plantId: 0,
  })

  const submit = async () => {
    const data = {...goal, complete: "N"};
    const res = await postData("/goal", data);
    addGoal(res);
    onClickGoalFormButton();
  }

  return (
    <Card style={{marginBottom: "1rem"}}>
      <div style={{marginBottom: "1rem"}}>
        <span style={{fontSize: "1rem"}} className="text-orange">목표 추가</span>
        <CloseOutlined className="float-end" style={{color: "grey"}} onClick={onClickGoalFormButton}/>
      </div>
      <div style={{marginBottom: "1rem"}}>
        <InputWithFeedback
          label="목표"
          labelColor={formLabelColor}
          size="small"
          onChange={(e) => setGoal(() => ({...goal, content: e.target.value}))}
          feedbackMsg={goal.content == "" ? "목표를 입력해주세요" : ""}/>
      </div>
      <div style={{marginBottom: "1rem"}}>
        <span className={`text-${formLabelColor}`} style={style}>식물</span>
        <SelectPlant
          onChange={(plantId) => setGoal(() => ({...goal, plantId: plantId}))}
          size="small"
          className="width-full"/>
        <InputFeedbackSpan feedbackMsg="비워둬도 괜찮아요" color="success"/>
      </div>
      <ValidationSubmitButton
        isValid={goal.content !== ""}
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
