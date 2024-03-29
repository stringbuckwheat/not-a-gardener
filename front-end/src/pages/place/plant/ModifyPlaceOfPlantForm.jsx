import {Button, Form, Select, Space} from "antd";
import React, {useEffect, useState} from "react";
import getData from "../../../api/backend-api/common/getData";
import updateData from "../../../api/backend-api/common/updateData";
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

  const onMountModifyPlaceOfPlantForm = async () => {
    const plantList = await getData("/plant");
    const plantOptions = [];

    for (let plant of plantList) {
      if (plant.placeId == placeId) {
        continue;
      }

      plantOptions.push({
        label: `${plant.name} (${plant.placeName})`,
        value: plant.id,
      })
    }

    setOptions(plantOptions);
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
    <Form layout="vertical" autoComplete="off">
      <Form.Item name="name" label="다른 장소의 식물 가져오기">
        <Select
          mode="multiple"
          allowClear
          onChange={handleChange}
          options={options}
        />
      </Form.Item>
      <Space style={{float: "right", marginBottom: "1rem"}}>
        <Button type={"text"} onClick={() => setMoveFormVisible(false)}>
          취소
        </Button>
        <Button onClick={submit}>제출</Button>
      </Space>
    </Form>
  )
}

export default ModifyPlaceOfPlantForm
