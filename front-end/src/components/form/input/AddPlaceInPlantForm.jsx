import {Alert, Form, Input, Select, Space, Button} from "antd";
import React, {useState} from "react";
import InputFeedbackSpan from "../../etc/InputFeedbackSpan";
import postData from "../../../api/backend-api/common/postData";

/**
 * 식물 추가 폼에서 장소 간단하게 추가하기
 * @param addPlace 장소 추가 요청 후 콜백 함수
 * @returns {JSX.Element}
 * @constructor
 */
const AddPlaceInPlantForm = ({addPlace}) => {
  const [isAddFormOpened, setIsAddFormOpened] = useState(false);
  const [placeName, setPlaceName] = useState("");
  const [msg, setMsg] = useState("");

  const submit = async () => {
    if (placeName == "") {
      setMsg("장소 이름을 입력해주세요");
      return;
    }

    const data = {
      name: placeName,
      option: "실내",
      artificialLight: "미사용"
    }

    const place = await postData("/place", data);
    console.log("place", place);
    addPlace({key: place.id, value: place.name});
  }

  return (
    <>
      <Form.Item
        label={"장소"}
        required={true}
      >
        <Select disabled={true} style={{width: "100%"}}/>
      </Form.Item>
      <Alert
        style={{marginTop: "0.25rem", marginBottom: "1rem"}}
        message={!isAddFormOpened ? "식물을 등록하려면 최소 하나의 장소가 필요해요." : ""}
        type="warning"
        action={
          <div>
            <Space>
              {isAddFormOpened
                ? <>
                  <InputFeedbackSpan feedbackMsg={msg}/>
                  <Input onChange={(e) => setPlaceName(e.target.value)} size={"small"}
                         placeholder={"장소 이름"} style={{width: "100%"}}/>
                </>
                : <></>}
              <Button
                type={"primary"}
                size={"small"}
                onClick={isAddFormOpened ? submit : () => setIsAddFormOpened(true)}
              >
                {"장소 추가"}
              </Button>
            </Space>
          </div>
        }
        showIcon={!isAddFormOpened}
      />
    </>
  )
}

export default AddPlaceInPlantForm
