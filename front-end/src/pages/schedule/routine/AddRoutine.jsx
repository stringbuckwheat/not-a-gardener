import CIcon from "@coreui/icons-react";
import {cilX} from "@coreui/icons";
import {Card, Checkbox, Input, Row, Select, Space} from "antd";
import React, {useState} from "react";
import SelectPlant from "../../../components/select/SelectPlant";
import InputFeedback from "../../../components/input/InputFeedback";
import ValidationSubmitButton from "../../../components/button/ValidationSubmitButton";
import postData from "../../../api/backend-api/common/postData";

const AddRoutine = ({plantList, onClickRoutineFormButton, addRoutine}) => {
  const style = {fontSize: "0.9em"}
  const formLabelColor = "garden"

  const [routine, setRoutine] = useState({
    routineContent: "",
    plantNo: 0,
    routineCycle: 0
  });
  const [checked, setChecked] = useState(false);

  const onChange = (e) => {
    const {name, value} = e.target;
    setRoutine(() => ({...routine, [name]: value}));
  }

  const onChangeSelect = (plantNo) => {
    setRoutine(() => ({...routine, plantNo: plantNo}));
  };

  const onChangeCheckbox = (e) => {
    setChecked(e.target.checked);

    if (e.target.checked) {
      setRoutine(() => ({...routine, routineCycle: 1}));
      return;
    }

    setRoutine(() => ({...routine, routineCycle: 0}));
  }

  const submit = async () => {
    const res = await postData("/routine", routine);
    addRoutine(res);
    onClickRoutineFormButton();
  }

  return (
    <Card className="mb-2">
      <div className="mb-2">
        <span className="fs-6 text-orange">루틴 추가</span>
        <CIcon
          icon={cilX}
          className="float-end"
          onClick={onClickRoutineFormButton}/>
      </div>
      <div className="mb-2">
        <InputFeedback
          name="routineContent"
          label="루틴 내용"
          labelColor={formLabelColor}
          size="small"
          onChange={onChange}
          // onSearch={onSearch}
          feedbackMsg={routine.routineContent == "" ? "루틴 내용은 비워둘 수 없어요" : ""}/>
      </div>
      <div className="mb-2">
        <span className={`text-${formLabelColor}`} style={style}>식물</span>
        <SelectPlant
          plantList={plantList}
          onChange={onChangeSelect}
          size="small"
          className="width-full"/>
      </div>
      <div className="mb-3">
        <span className={`text-${formLabelColor}`} style={style}>루틴 주기</span>
        <Row>
          <Space>
            {
              checked
                ? <Input size="small" value="매일" disabled={true}/>
                :
                <>
                  <Input name="routineCycle" size="small" type="number" className="col" onChange={onChange}/>
                  <span className="col" style={style} className={`text-${formLabelColor}`}>일에 한 번</span>
                </>
            }
          </Space>
        </Row>
        <Checkbox onChange={onChangeCheckbox}><span style={style}
                                                    className={`text-${formLabelColor}`}>매일</span></Checkbox>
      </div>

      <ValidationSubmitButton
        isValid={routine.routineContent !== "" && routine.plantNo !== 0 && routine.routineCycle !== 0}
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
