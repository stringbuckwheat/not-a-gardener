import {SearchOutlined} from "@ant-design/icons";
import {Input} from "antd";
import {useEffect, useRef} from "react";

const SearchInput = ({setSearch, setSearchWord}) => {
  // 오토 포커스
  const searchInput = useRef(); // DOM 요소를 searchElement에 할당

  useEffect(() => {
    if (searchInput.current) {
      searchInput.current.focus();
    }
  }, [searchInput])

  const onBlur = () => {
    setSearch(false);
  }

  return (
    <Input
      ref={searchInput}
      addonBefore={<SearchOutlined/>}
      className="width-200"
      onChange={(e) => {
        setSearchWord(e.target.value)
      }}
      onBlur={onBlur}
      allowClear/>
  )
}

export default SearchInput
