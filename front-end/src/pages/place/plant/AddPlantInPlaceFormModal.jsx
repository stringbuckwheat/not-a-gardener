import getPlantFormArrayWithPlaceName from "../../../utils/function/getPlantFormArrayWithPlaceName";
import ItemForm from "../../../components/form/ItemForm";
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
const AddPlantInPlaceFormModal = ({visible, callBackFunction, placeNo, placeName, setAddPlantFormVisible}) => {
  const [plant, setPlant] = useState({
    plantName: "",
    plantSpecies: "",
    placeNo: placeNo,
    medium: "흙과 화분",
    earlyWateringPeriod: 0,
    birthday: ""
  });

  const onChange = (e) => {
    const {name, value} = e.target;
    setPlant(setPlant => ({...plant, [name]: value}));
  }

  const submit = async () => {
    const res = await postData(`/place/${placeNo}/plant`, plant);
    callBackFunction(res);
  }

  const isValid = plant.plantName != '';

  const [feedbackMsg, setFeedbackMsg] = useState(false);

  return (
    <ConfigProvider theme={{token: {colorPrimary: '#6d9773'}}}>
      <Modal title="이 장소에 식물을 추가할래요"
             open={visible}
             okText="추가"
             okButtonProps={{onClick: isValid ? submit : () => setFeedbackMsg(true)}}
             cancelText="돌아가기"
             onCancel={() => setAddPlantFormVisible(false)}
      >
        <ItemForm
          inputObject={plant}
          itemObjectArray={getPlantFormArrayWithPlaceName(placeName)}
          onChange={onChange}
        />
        {
          feedbackMsg
            ?
            <div className="d-flex justify-content-end">
              <InputFeedbackSpan feedbackMsg="식물 이름은 비워둘 수 없어요" color="danger"/>
            </div>
            : <></>
        }
      </Modal>
    </ConfigProvider>
  )
}

export default AddPlantInPlaceFormModal;
