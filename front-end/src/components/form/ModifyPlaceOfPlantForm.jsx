import {Form, Select, Space} from "antd";
import {CButton} from "@coreui/react";
import {useEffect, useState} from "react";
import getData from "../../api/backend-api/common/getData";
import modifyPlantPlace from "../../api/backend-api/place/modifyPlantPlace";
import {useNavigate} from "react-router-dom";

const ModifyPlaceOfPlantForm = (props) => {
  const {placeNo, setMoveFormVisible} = props;
  const [options, setOptions] = useState([{}])
  const [selectedPlantList, setSelectedPlantList] = useState([]);

  const navigate = useNavigate();

  const onMountModifyPlaceOfPlantForm = async () => {
    const res = await getData("/plant");
    const exceptHere = res.filter(plant => plant.placeNo != placeNo);
    const options = exceptHere.map((plant) => (
      {
        label: `${plant.plantName} (${plant.placeName})`,
        value: plant.plantNo,
      }
    ))

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
    const data = {placeNo: placeNo, plantList: selectedPlantList}
    await modifyPlantPlace(data); // void

    const res = await getData(`/place/${placeNo}`)
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
