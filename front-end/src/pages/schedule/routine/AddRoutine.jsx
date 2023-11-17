import {Card, Checkbox, Input, Row, Space} from "antd";
import React, {useState} from "react";
import SelectPlant from "../../../components/select/SelectPlant";
import InputWithFeedback from "../../../components/form/input/InputWithFeedback";
import ValidationSubmitButton from "../../../components/button/ValidationSubmitButton";
import postData from "../../../api/backend-api/common/postData";
import {CloseOutlined} from "@ant-design/icons";

const AddRoutine = ({onClickRoutineFormButton, addRoutine}) => {
  const style = {fontSize: "0.9em"}
  const formLabelColor = "garden"

  const [routine, setRoutine] = useState({
    content: "",
    plantId: 0,
    cycle: 0
  });

  const [checked, setChecked] = useState(false);

  const onChange = (e) => {
    const {name, value} = e.target;
    setRoutine(() => ({...routine, [name]: value}));
  }

  const onChangeCheckbox = (e) => {
    setChecked(e.target.checked);
    const data = e.target.checked ? {...routine, cycle: 1} : {...routine, cycle: 0};
    setRoutine(() => data);
  }

  const submit = async () => {
    const res = await postData("/routine", routine);
    addRoutine(res);
    onClickRoutineFormButton();
  }

  const getRoutineCycleFeedbackMsg = () => {
    if (routine.content == "") return "루틴 내용은 비워둘 수 없어요"
    else if (!Number.isInteger(routine.content)) return "숫자를 입력해주세요"
    else if (routine.content * 1 <= 0) return "루틴 주기를 확인해주세요"
  }

  return (
    <Card style={{marginBottom: "1rem"}}>
      <div style={{marginBottom: "1rem"}}>
        <span style={{fontSize: "1rem"}} className="text-orange">루틴 추가</span>
        <CloseOutlined className="float-end" style={{color: "grey"}} onClick={onClickRoutineFormButton}/>
      </div>
      <div style={{marginBottom: "1rem"}}>
        <InputWithFeedback
          name="content"
          label="루틴 내용"
          labelColor={formLabelColor}
          size="small"
          onChange={onChange}
          feedbackMsg={routine.content == "" ? "루틴 내용은 비워둘 수 없어요" : ""}/>
      </div>
      <div style={{marginBottom: "1rem"}}>
        <span className={`text-${formLabelColor}`} style={style}>식물</span>
        <SelectPlant
          onChange={(plantId) => {
            setRoutine(() => ({...routine, plantId: plantId}))
          }}
          size="small"
          className="width-full"/>
      </div>
      <div style={{marginBottom: "1rem"}}>
        <Row>
          <Space>
            {
              checked
                ?
                <>
                  <span className={`text-${formLabelColor}`} style={style}>루틴 주기</span>
                  <Input size="small" value="매일" disabled={true}/>
                </>
                :
                <>
                  <InputWithFeedback
                    name="cycle"
                    type="number"
                    label="루틴 주기"
                    labelColor={formLabelColor}
                    size="small"
                    onChange={onChange}
                    feedbackMsg={getRoutineCycleFeedbackMsg()}/>
                  <span style={style} className={`text-${formLabelColor}`}>일에 한 번</span>
                </>
            }
          </Space>
        </Row>
        <Checkbox onChange={onChangeCheckbox}><span style={style}
                                                    className={`text-${formLabelColor}`}>매일</span></Checkbox>
      </div>

      <ValidationSubmitButton
        isValid={routine.content !== "" && routine.plantId !== 0 && routine.cycle !== 0}
        onClickValid={submit}
        onClickInvalidMsg="입력 값을 확인해주세요"
        title="추가"
        className="float-end"
        size="small"
      />
    </Card>
  )
}

export default AddRoutine
