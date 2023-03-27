import {useState} from "react";
import GardenCardDetail from "./GardenCardDetail";
import "../../../pages/garden/GardenList.module.css";

const GardenCard = (props) => {
  const garden = props.garden;

  const setPlantList = props.setPlantList;
  const openNotification = props.openNotification;

  const [watered, setWatered] = useState(false);


  return (
    <div className={"parent"}>
      <div className={"child"}>
        <GardenCardDetail garden={garden}/>
      </div>
    </div>
  )
}

export default GardenCard;
