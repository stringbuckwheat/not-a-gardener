import React, {useState} from "react";
import forNoPlant from "../../assets/images/forNoPlant.png";
import getPlaceListForSelect from "../../api/service/getPlaceListForSelect";
import AddPlant from "../../pages/plant/AddPlant";
import {Button} from "antd";
import Style from "./Empty.module.scss"

/**
 * 식물 없음 페이지
 * @param addPlant
 * @param afterAdd
 * @returns {JSX.Element}
 * @constructor
 */
const NoItemForPlant = ({addPlant, afterAdd}) => {
  const [isAddFormOpened, setIsAddFormOpened] = useState(false);
  const [placeList, setPlaceList] = useState([]);

  const onClick = async () => {
    const placeList = await getPlaceListForSelect();
    setPlaceList(() => placeList);
    setIsAddFormOpened(true);
  }

  return isAddFormOpened ? (
    <AddPlant placeList={placeList} addPlant={addPlant} afterAdd={afterAdd}/>
  ) : (
    <div style={{textAlign: "center"}}>
      <h2 className={Style.title}>등록된 식물이 없어요</h2>
      <div className={Style.long}>
        <Button
          className={Style.button}
          onClick={onClick}>
          식물 등록하기
        </Button>
      </div>
      <img src={forNoPlant}/>
    </div>
  )
}

export default NoItemForPlant;
