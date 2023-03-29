import {CRow} from "@coreui/react";
import {useEffect, useState} from "react";
import AddItemCard from "src/components/card/AddItemCard";
import PlaceCard from "src/components/card/PlaceCard";
import ListHeader from "src/components/data/header/ListHeader";

const PlaceList = (props) => {
  const {placeList, setPlaceList, originPlaceList} = props;

  // 검색
  const [searchWord, setSearchWord] = useState("");

  const search = (searchWord) => {
    const searchedList = originPlaceList.filter(place => place.placeName.includes(searchWord));
    setPlaceList(searchedList);
  }

  useEffect(() => {
    if (searchWord !== "") {
      search(searchWord)
    } else {
      setPlaceList(originPlaceList);
    }
  }, [searchWord])

  /// 정렬
  const [sort, setSort] = useState("");

  const sortOption = [
    {value: 'createDate', label: '생성일순'},
    {value: 'abc', label: '가나다순'},
    {value: 'manyPlant', label: '식물 많은순'},
    {value: 'createDateDesc', label: '최근 생성순'},
  ]

  useEffect(() => {
    const sortedPlaceList = [...originPlaceList];

    if (sort === "abc") {
      sortedPlaceList.sort((a, b) => (a.placeName < b.placeName ? -1 : a.placeName > b.placeName ? 1 : 0))
    } else if (sort === "manyPlant") {
      sortedPlaceList.sort((a, b) => b.plantListSize - a.plantListSize);
    } else if (sort === "createDate") {
      sortedPlaceList.sort((a, b) => new Date(a.createDate) - new Date(b.createDate));
    } else if (sort === "createDateDesc") {
      sortedPlaceList.sort((a, b) => new Date(b.createDate) - new Date(a.createDate));
    }

    setPlaceList(sortedPlaceList);
  }, [sort])

  return (
    <>
      <ListHeader
        setSearchWord={setSearchWord}
        sortOption={sortOption}
        setSort={setSort}/>
      <CRow>
        <AddItemCard addUrl="/place/add" addMsg="새로운 장소 추가"/>

        {/* 카드 컴포넌트 반복 */}
        {placeList.map((place) => (
          <PlaceCard place={place}/>
        ))}
      </CRow>
    </>
  )
}

export default PlaceList;
