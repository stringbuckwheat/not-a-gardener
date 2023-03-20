import { CRow } from "@coreui/react";
import { useEffect, useState } from "react";
import onMount from "src/api/service/onMount";
import AddItemCard from "src/components/card/AddItemCard";
import PlaceCard from "src/components/card/PlaceCard";
import ListHeader from "src/components/data/header/ListHeader";

const PlaceList = (props) => {
    const placeList = props.placeList;
    const setPlaceList = props.setPlaceList;

    const [originPlaceList, setOriginPlaceList] = useState([]);
    const [searchWord, setSearchWord] = useState("");

    useEffect(() => {
        onMount("/place", setOriginPlaceList);
    }, [])

    const search = async (searchWord) => {
        const searchedList = originPlaceList.filter(place => place.placeName.includes(searchWord));
        setPlaceList(searchedList);
    }

    useEffect(() => {
        console.log("현재 검색어: ", searchWord);
        if (searchWord !== "") {
            search(searchWord)
        } else {
            ////
            setPlaceList(originPlaceList);
        }
    }, [searchWord])

    /// 정렬
    const [sort, setSort] = useState("");

    const sortOption = [
        { value: 'abc', label: '가나다순' },
        { value: 'manyPlant', label: '식물 많은순' },
        { value: 'createDate', label: '생성일순' },
        { value: 'createDateDesc', label: '최근 생성순' },
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
                setSort={setSort} />
            <CRow>
                <AddItemCard addUrl="/place/add" addMsg="새로운 장소 추가" />

                {/* 카드 컴포넌트 반복 */}
                {placeList.map((place) => (
                    <PlaceCard place={place} />
                ))}
            </CRow>
        </>
    )
}

export default PlaceList;