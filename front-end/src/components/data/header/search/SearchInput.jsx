import {SearchOutlined} from "@ant-design/icons";
import {Input} from "antd";
import {useEffect, useRef} from "react";

/**
 * 검색창
 * @param setIsSearchFormOpened
 * @param setSearchWord
 * @returns {JSX.Element}
 * @constructor
 */
const SearchInput = ({setIsSearchFormOpened, setSearchWord}) => {
  // 오토 포커스
  const searchInput = useRef(); // DOM 요소를 searchElement에 할당

  useEffect(() => {
    if (searchInput.current) {
      searchInput.current.focus();
    }
  }, [searchInput])

  return (
    <Input
      ref={searchInput}
      addonBefore={<SearchOutlined/>}
      className="width-200"
      onChange={(e) => setSearchWord(e.target.value)}
      onBlur={() => setIsSearchFormOpened(false)}
      allowClear/>
  )
}

export default SearchInput
