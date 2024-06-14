import React, {useState} from "react";
import forNoPlant from "../../assets/images/forNoPlant.png";
import getPlaceListForSelect from "../../api/service/getPlaceListForSelect";
import AddPlant from "../../pages/plant/AddPlant";
import {Button, ConfigProvider} from "antd";
import Style from "./Empty.module.scss"
import {SmileOutlined} from "@ant-design/icons";
import Booped from "../animation/Booped";
import themeGreen from "../../theme/themeGreen";

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
    <ConfigProvider theme={themeGreen}>
      <div style={{textAlign: "center"}}>
        <h2 className={Style.title}>
          반가워요!
          <Booped rotation={20} timing={200}>
            <SmileOutlined style={{fontSize: "2rem", color: "green", marginLeft: "0.5rem"}}/>
          </Booped>
        </h2>
        <div className={Style.long}>
          <button
            className={Style.button}
            onClick={onClick}>
            식물 등록하기
          </button>
        </div>
        <img src={forNoPlant}/>
      </div>
    </ConfigProvider>
  )
}

export default NoItemForPlant;
