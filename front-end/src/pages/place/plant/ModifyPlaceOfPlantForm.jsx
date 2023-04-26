import {Form, Select, Space} from "antd";
import {CButton} from "@coreui/react";
import {useEffect, useState} from "react";
import getData from "../../../api/backend-api/common/getData";
import modifyPlantPlace from "../../../api/backend-api/place/modifyPlantPlace";
import {useNavigate} from "react-router-dom";

/**
 * 장소 페이지에서 다른 장소에 있는 식물을 이 장소로 이동하는 form
 * '다른 장소의 식물 이동'을 눌렀을 시 실행됨
 * 부모 컴포넌트: AddPlantInPlaceButtons
 * @param placeNo
 * @param setMoveFormVisible
 * @returns {JSX.Element}
 * @constructor
 */
const ModifyPlaceOfPlantForm = ({placeNo, setMoveFormVisible}) => {
  const [options, setOptions] = useState([{}])
  const [selectedPlantList, setSelectedPlantList] = useState([]);

  const navigate = useNavigate();

  const onMountModifyPlaceOfPlantForm = async () => {
    const plantList = await getData("/plant");
    const options = [];

    for (let plant of plantList) {
      if (plant.placeNo == placeNo) {
        continue;
      }

      options.push({
        label: `${plant.plantName} (${plant.placeName})`,
        value: plant.plantNo,
      })
    }

    setOptions(options);
  }

  useEffect(() => {
    onMountModifyPlaceOfPlantForm();
  }, [])

  const handleChange = async (value) => {
    setSelectedPlantList(value);
  };

  const submit = async () => {
    // 장소 업데이트
    await modifyPlantPlace({placeNo: placeNo, plantList: selectedPlantList});
    const res = await getData(`/place/${placeNo}`);

    setMoveFormVisible(false);
    navigate("", {replace: true, state: res})
  }

  return (
    <Form className="mb-5" layout="vertical" autoComplete="off">
      <Form.Item name="name" label="이 장소에 식물 추가하기" className="mb-2">
        <Select
          mode="multiple"
          allowClear
          onChange={handleChange}
          options={options}
        />
      </Form.Item>
      <Space className="float-end">
        <CButton
          size="sm"
          variant="outline"
          onClick={() => {
            setMoveFormVisible(false)
          }}
          color="dark">취소</CButton>
        <CButton
          size="sm"
          variant="outline"
          onClick={submit}
          color="success">제출</CButton>
      </Space>
    </Form>
  )
}

export default ModifyPlaceOfPlantForm
