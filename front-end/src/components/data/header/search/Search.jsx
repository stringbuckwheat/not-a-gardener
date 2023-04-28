import {SearchOutlined} from "@ant-design/icons";
import {useState} from "react";
import SearchInput from "./SearchInput";

/**
 * 검색버튼 -> 클릭 -> 검색창
 * @param setSearchWord
 * @returns {JSX.Element}
 * @constructor
 */
const Search = ({setSearchWord}) => {
  const [isSearchFormOpened, setIsSearchFormOpened] = useState(false);

  return isSearchFormOpened ? (
    <SearchInput
      setIsSearchFormOpened={setIsSearchFormOpened}
      setSearchWord={setSearchWord}/>
  ) : (
    <SearchOutlined
      className="vertical-align-middle"
      onClick={() => setIsSearchFormOpened(true)}/>
  )
}

export default Search;
