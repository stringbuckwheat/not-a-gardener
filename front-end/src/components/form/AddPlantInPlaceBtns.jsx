import {useState} from "react";
import {Space} from 'antd';
import {CButton} from "@coreui/react";
import {useParams} from "react-router-dom";
import ModifyPlaceOfPlantForm from "./ModifyPlaceOfPlantForm";
import AddModal from "../modal/AddModal";

const AddPlantInPlaceBtns = ({placeName, setPlantList}) => {
  const placeNo = useParams().placeNo;

  const [moveFormVisible, setMoveFormVisible] = useState(false);

  const [addPlantFormVisible, setAddPlantFormVisible] = useState(false);
  const closeModal = () => {
    setAddPlantFormVisible(false);
  }

  const onClickAddPlantBtn = async () => {
    setAddPlantFormVisible(true);
    setMoveFormVisible(false);
  }

  const callBackFunction = (res) => {
    setAddPlantFormVisible(false);
    setPlantList(plantList => [res, ...plantList]);
  }

  return (
    <>
      {
        moveFormVisible
          ?
          <ModifyPlaceOfPlantForm placeNo={placeNo} setMoveFormVisible={setMoveFormVisible}/>
          :
          addPlantFormVisible
            ? <AddModal
              placeName={placeName}
              visible={addPlantFormVisible}
              closeModal={closeModal}
              callBackFunction={callBackFunction}
              placeNo={placeNo}
              setAddPlantFormVisible={setAddPlantFormVisible}/>
            :
            <div className="float-end mb-4">
              <Space>
                <CButton
                  size="sm"
                  variant="outline"
                  onClick={onClickAddPlantBtn}
                  color="success">새 식물 추가</CButton>
                <CButton
                  size="sm"
                  variant="outline"
                  onClick={() => {
                    setMoveFormVisible(true)
                    setAddPlantFormVisible(false)
                  }}
                  color="success">다른 장소의 식물 이동</CButton>
              </Space>
            </div>
      }
    </>
  )
}

export default AddPlantInPlaceBtns;
