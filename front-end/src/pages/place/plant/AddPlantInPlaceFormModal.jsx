import getPlantFormArrayWithPlaceName from "../../../utils/function/getPlantFormArrayWithPlaceName";
import {useState} from "react";
import postData from "../../../api/backend-api/common/postData";
import {ConfigProvider, Form, Modal, Space, Tooltip} from "antd";
import InputFeedbackSpan from "../../../components/etc/InputFeedbackSpan";
import {QuestionCircleTwoTone} from "@ant-design/icons";
import InputHandler from "../../../components/form/InputHandler";

/**
 * 장소에서 새 식물 추가하기 폼
 * => 부모 컴포넌트: AddPlantInPlaceButtons
 * @param visible
 * @param callBackFunction
 * @param placeNo
 * @param placeName
 * @param setAddPlantFormVisible
 * @returns {JSX.Element}
 * @constructor
 */
const AddPlantInPlaceFormModal = ({visible, callBackFunction, placeId, placeName, setAddPlantFormVisible}) => {
  const [plant, setPlant] = useState({
    name: "",
    species: "",
    placeId: placeId,
    medium: "흙과 화분",
    recentWateringPeriod: 0,
    birthday: ""
  });

  const onChange = (e) => {
    const {name, value} = e.target;
    setPlant(setPlant => ({...plant, [name]: value}));
  }

  const submit = async () => {
    const res = await postData(`/place/${placeId}/plant`, plant);

    callBackFunction({
      key: res.id,
      id: res.id,
      name: res.name,
      species: res.species,
      recentWateringPeriod:
        res.recentWateringPeriod == 0
          ? <Space align="middle"><Tooltip title={"물주기를 알아가는 중이에요"}><QuestionCircleTwoTone/></Tooltip></Space>
          : res.recentWateringPeriod,
      tags: [res.medium],
      createDate: res.createDate
    });
  }

  const isValid = plant.name != '' &&
    (!plant.recentWateringPeriod || (Number.isInteger(plant.recentWateringPeriod * 1) && plant.recentWateringPeriod * 1 >= 0));
  const getFeedbackMsg = () => {
    if (plant.name == "") return "식물 이름은 비워둘 수 없어요"
    else if (!Number.isInteger(plant.recentWateringPeriod) || plant.recentWateringPeriod * 1 < 0) return "0 이상의 정수를 입력해주세요";
  }

  const [feedbackMsg, setFeedbackMsg] = useState(false);
  const modalTitle = <div><span className={"text-orange"}>{placeName}</span>에 식물을 추가할래요</div>;

  return (
    <ConfigProvider theme={{token: {colorPrimary: '#6d9773'}}}>
      <Modal title={modalTitle}
             open={visible}
             okText="추가"
             okButtonProps={{onClick: isValid ? submit : () => setFeedbackMsg(true)}}
             cancelText="돌아가기"
             style={{padding: "1rem"}}
             onCancel={() => setAddPlantFormVisible(false)}
      >
        <Form layout="vertical">
          <InputHandler
            itemObjectArray={getPlantFormArrayWithPlaceName(placeName)}
            onChange={onChange}
            inputObject={plant}/>
        </Form>
        {
          feedbackMsg
            ?
            <div style={{display: "flex", justifyContent: "flex-end"}}>
              <InputFeedbackSpan feedbackMsg={getFeedbackMsg()} color="danger"/>
            </div>
            : <></>
        }
      </Modal>
    </ConfigProvider>
  )
}

export default AddPlantInPlaceFormModal;
