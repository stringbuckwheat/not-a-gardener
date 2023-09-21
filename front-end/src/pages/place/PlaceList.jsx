import {CRow} from "@coreui/react";
import {useState} from "react";
import AddItemCard from "src/components/card/AddItemCard";
import PlaceCard from "src/pages/place/PlaceCard";
import AddPlace from "./AddPlace";
import {useSelector} from "react-redux";

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

  return isAddFormOpened ? (
    <AddPlace
      afterAdd={switchAddForm}
    />
  ) : (
    <>
      <CRow>
        <AddItemCard
          addUrl="/place/add"
          addMsg="새로운 장소 추가"
          onClick={switchAddForm}/>
        {/* 카드 컴포넌트 반복 */}
        {places.map((place) => (
          <PlaceCard place={place} key={place.id}/>
        ))}
      </CRow>
    </>
  )
}

export default PlaceList;
