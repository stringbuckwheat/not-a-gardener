import getPlantFormArrayWithPlaceName from "../../../utils/function/getPlantFormArrayWithPlaceName";
import FormProvider from "../../../components/form/FormProvider";
import {useState} from "react";
import postData from "../../../api/backend-api/common/postData";
import {ConfigProvider, Modal} from "antd";
import InputFeedbackSpan from "../../../components/etc/InputFeedbackSpan";

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
    callBackFunction(res);
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
             onCancel={() => setAddPlantFormVisible(false)}
      >
        <FormProvider
          inputObject={plant}
          itemObjectArray={getPlantFormArrayWithPlaceName(placeName)}
          onChange={onChange}
        />
        {
          feedbackMsg
            ?
            <div className="d-flex justify-content-end">
              <InputFeedbackSpan feedbackMsg={getFeedbackMsg()} color="danger"/>
            </div>
            : <></>
        }
      </Modal>
    </ConfigProvider>
  )
}

export default AddPlantInPlaceFormModal;
