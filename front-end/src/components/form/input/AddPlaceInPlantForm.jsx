import {CButton, CFormSelect, CInputGroup, CInputGroupText} from "@coreui/react";
import {Alert, Input, Space} from "antd";
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
    }

    const data = {
      name: placeName,
      option: "실내",
      artificialLight: "미사용"
    }

    const place = await postData("/place", data);
    addPlace({key: place.id, value: place.name});
  }

  return (
    <>
      <CInputGroup>
        <CInputGroupText id="basic-addon1">{"장소"}</CInputGroupText>
        <CFormSelect disabled/>
      </CInputGroup>
      <Alert
        className="mt-1 mb-3"
        message={!isAddFormOpened ? "등록된 장소가 없습니다." : ""}
        type="warning"
        action={
          <div>
            <Space>
              {isAddFormOpened
                ? <>
                  <InputFeedbackSpan feedbackMsg={msg}/>
                  <Input onChange={(e) => setPlaceName(e.target.value)} size={"small"}
                         placeHolder={"장소 이름"} style={{width: "100%"}}/>
                </>
                : <></>}
              <CButton
                color="success"
                onClick={isAddFormOpened ? submit : () => setIsAddFormOpened(true)}
                size={"sm"}
                variant="outline"
                shape="rounded-pill">
                {"장소 추가"}
              </CButton>
            </Space>
          </div>
        }
        showIcon={!isAddFormOpened}
      />
    </>
  )
}

export default AddPlaceInPlantForm
