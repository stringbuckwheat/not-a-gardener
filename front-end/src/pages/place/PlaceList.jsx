import React, {useState} from "react";
import AddItemCard from "src/components/card/AddItemCard";
import AddPlace from "./AddPlace";
import {useSelector} from "react-redux";
import {BulbOutlined, HomeOutlined, MergeCellsOutlined, PictureOutlined} from "@ant-design/icons";
import ListItemCard from "../../components/card/ListItemCard";
import {Row} from "antd";

/**
 * 장소 (카드) 리스트
 * @param placeList
 * @param setPlaceList
 * @param originPlaceList
 * @param setOriginPlaceList
 * @returns {JSX.Element}
 * @constructor
 */
const PlaceList = () => {
  const places = useSelector((state) => state.places.places);
  console.log("places", places);

  const [isAddFormOpened, setAddFormOpened] = useState(false);
  const switchAddForm = () => setAddFormOpened(!isAddFormOpened);

  const getPlaceForCard = (place) => {
    let color = "";
    let icon = {};

    const style = {
      padding: "1.2rem",
      color: "white",
      fontSize: "2rem",
      borderRadius: "8px"
    }

    if (place.option === "실내") {
      color = "green";
      icon = <HomeOutlined style={{...style, backgroundColor: color}}/>;
    } else if (place.option === "베란다") {
      color = "#007BFF";
      icon = <MergeCellsOutlined style={{...style, backgroundColor: color}}/>;
    } else if (place.option === "야외") {
      color = "orange";
      icon = <PictureOutlined style={{...style, backgroundColor: color}}/>;
    }

    const name = place.artificialLight === "사용"
      ?
      <div style={{display: "flex", justifyContent: "space-between"}}>
        <span>{place.name}</span>
        <BulbOutlined/>
      </div>
      : <div>{place.name}</div>

    return {color, icon, name}
  }

  return isAddFormOpened ? (
    <AddPlace
      afterAdd={switchAddForm}
    />
  ) : (
    <>
      <Row>
        <AddItemCard
          addUrl="/place/add"
          addMsg="새로운 장소 추가"
          onClick={switchAddForm}/>
        {/* 카드 컴포넌트 반복 */}
        {places.map((place) => {
          const placeForCard = getPlaceForCard(place);

          return <ListItemCard color={placeForCard.color}
                               icon={placeForCard.icon}
                               link={`/place/${place.id}`}
                               name={placeForCard.name}
                               type={place.option}
                               detail={`${place.plantListSize}개의 식물이 살고 있어요`}/>
        })}
      </Row>
    </>
  )
}

export default PlaceList;
