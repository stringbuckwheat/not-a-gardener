import {Form, Select, Space, Tooltip} from "antd";
import {CButton} from "@coreui/react";
import {useEffect, useState} from "react";
import getData from "../../../api/backend-api/common/getData";
import {useNavigate} from "react-router-dom";
import updateData from "../../../api/backend-api/common/updateData";
import {QuestionCircleTwoTone} from "@ant-design/icons";
import getPlantListForPlacePlantTable from "../../../utils/function/getPlantListForPlacePlantTable";

/**
 * 장소 페이지에서 다른 장소에 있는 식물을 이 장소로 이동하는 form
 * '다른 장소의 식물 이동'을 눌렀을 시 실행됨
 * 부모 컴포넌트: AddPlantInPlaceButtons
 * @param placeId
 * @param setMoveFormVisible
 * @returns {JSX.Element}
 * @constructor
 */
const ModifyPlaceOfPlantForm = ({placeId, setMoveFormVisible, setPlants}) => {
  // 식물 select용 options 배열 저장
  const [options, setOptions] = useState([{}])
  const [selectedPlantList, setSelectedPlantList] = useState([]);

  const navigate = useNavigate();

  const onMountModifyPlaceOfPlantForm = async () => {
    const plantList = await getData("/plant");
    const options = [];

    for (let plant of plantList) {
      if (plant.placeId == placeId) {
        continue;
      }

      options.push({
        label: `${plant.name} (${plant.placeName})`,
        value: plant.id,
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
    await updateData(`/plant/place/${placeId}`, {placeId, plants: selectedPlantList});
    const res = await getData(`/place/${placeId}/plant?page=0`);

    setPlants(() => getPlantListForPlacePlantTable(res));
    setMoveFormVisible(false);
  }

  return (
    <Form className="mb-5" layout="vertical" autoComplete="off">
      <Form.Item name="name" label="다른 장소의 식물 가져오기" className="mb-2">
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
