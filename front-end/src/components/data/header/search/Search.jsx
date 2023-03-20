import { SearchOutlined } from "@ant-design/icons";
import { useState } from "react";
import SearchInput from "./SearchInput";

// 검색버튼을 보여주거나 검색창을 보여줌
const Search = (props) => {
    const setSearchWord = props.setSearchWord;

    const [search, setSearch] = useState(false);

    return (
        search
        ? <SearchInput 
                setSearch={setSearch}
                setSearchWord={setSearchWord}/>
        : 
        <SearchOutlined onClick={() => {setSearch(true)}} />
    )
}

export default Search;