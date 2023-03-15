import { SearchOutlined } from "@ant-design/icons";
import { Input } from "antd";
import { useEffect, useRef, useState } from "react";
import onMount from "src/api/service/onMount";

const SearchInput = (props) => {
    const setSearch = props.setSearch;
    const setPlaceList = props.setPlaceList;

    // 오토 포커스
    const searchInput = useRef(); // DOM 요소를 searchElement에 할당
    useEffect(() => {
        if (searchInput.current) {
            searchInput.current.focus();
        }
    }, [searchInput])

    // 검색할 전체 장소리스트
    const [originPlaceList, setOriginPlaceList] = useState([{}]);
    useEffect(() => {
        onMount("/place", setOriginPlaceList);
    }, [])

    // 검색 함수
    const onSearch = (e) => {
        const searchPlaceList = originPlaceList.filter(
            place => place.placeName.includes(e.target.value)
        )

        // 부모 컴포넌트 state 변경
        setPlaceList(searchPlaceList);
    }

    const onBlur = () => {
        setPlaceList(originPlaceList);
        setSearch(false)
    }

    return (
        <Input
                ref={searchInput}
                addonBefore={<SearchOutlined />}
                style={{ width: 200, }}
                onChange={onSearch}
                onBlur={onBlur}
                allowClear />
    )
}

export default SearchInput