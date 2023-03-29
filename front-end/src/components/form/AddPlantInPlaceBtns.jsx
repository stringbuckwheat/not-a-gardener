import {useEffect, useState} from "react";
import {Space} from 'antd';
import {CButton} from "@coreui/react";
import {useParams} from "react-router-dom";
import ModifyPlaceOfPlantForm from "./ModifyPlaceOfPlantForm";
import AddModal from "../modal/AddModal";
import getPlaceListForSelect from "../../api/service/getPlaceListForSelect";

const AddPlantInPlaceBtns = (props) => {
  const {plantList, setPlantList} = props;
  const placeNo = useParams().placeNo;

  const [moveFormVisible, setMoveFormVisible] = useState(false);

  const [addPlantFormVisible, setAddPlantFormVisible] = useState(false);
  const closeModal = () => {
    setAddPlantFormVisible(false);
  }

  const plant = {
    plantName: "",
    plantSpecies: "",
    placeNo: 0,
    medium: "흙과 화분",
    earlyWateringPeriod: 0,
    birthday: ""
  };

  const onClickAddPlantBtn = async () => {
    setAddPlantFormVisible(true);
    setMoveFormVisible(false);
    console.log("modal 오픈")
  }

  const [placeList, setPlaceList] = useState([]);

  const getPlaceList = async () => {
    const data = await getPlaceListForSelect();
    setPlaceList(data);
  }

  useEffect(() => {
    getPlaceList();
  }, [])

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
              visible={addPlantFormVisible}
              placeList={placeList}
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
