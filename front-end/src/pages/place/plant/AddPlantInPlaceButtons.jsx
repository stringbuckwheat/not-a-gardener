import {useState} from "react";
import {Button, Space} from 'antd';
import {useParams} from "react-router-dom";
import ModifyPlaceOfPlantForm from "./ModifyPlaceOfPlantForm";
import AddPlantInPlaceFormModal from "./AddPlantInPlaceFormModal";

/**
 * 장소 페이지 내 식물 리스트 위 새 식물을 추가하거나 이 장소에 식물을 수정할 버튼들
 * => 부모 컴포넌트: PlaceTableForPlant.jsx
 * @param placeName
 * @param setPlantList
 * @returns {JSX.Element} ModifyPlaceOfPlantForm || AddPlantInPlaceFormModal || 버튼들
 * @constructor
 */
const AddPlantInPlaceButtons = ({placeName, setPlants}) => {
  const placeId = useParams().placeId;

  const [addPlantFormVisible, setAddPlantFormVisible] = useState(false);
  const [moveFormVisible, setMoveFormVisible] = useState(false);

  const onClickAddPlantBtn = async () => {
    setAddPlantFormVisible(true);
    setMoveFormVisible(false);
  }

  const callBackFunction = (res) => {
    setAddPlantFormVisible(false);
    setPlants(plantList => [res, ...plantList]);
  }

  if (addPlantFormVisible) {
    // 새 식물 추가를 누른 경우
    return <AddPlantInPlaceFormModal
      placeName={placeName}
      visible={addPlantFormVisible}
      callBackFunction={callBackFunction}
      placeId={placeId}
      setAddPlantFormVisible={setAddPlantFormVisible}/>
  } else if (moveFormVisible) {
    // 다른 장소의 식물 이동을 누른 경우
    return <ModifyPlaceOfPlantForm placeId={placeId} setMoveFormVisible={setMoveFormVisible} setPlants={setPlants}/>
  }

  // 아무것도 선택하지 않았을 시 버튼들 렌더링
  return (
    <div style={{float: "right", marginBottom: "1rem"}}>
      <Space>
        <Button onClick={onClickAddPlantBtn}>새 식물 추가 </Button>
        <Button
          onClick={() => {
            setMoveFormVisible(true)
            setAddPlantFormVisible(false)
          }}>다른 장소의 식물 이동</Button>
      </Space>
    </div>
  )
}

export default AddPlantInPlaceButtons;
