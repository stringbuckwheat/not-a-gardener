import {Space} from "antd";
import Search from "./search/Search";
import Sort from "./sort/Sort";

/**
 * 검색/정렬 헤더
 * @param setSearchWord
 * @param sortOption
 * @param setSort
 * @returns {JSX.Element}
 * @constructor
 */
const ListHeader = ({setSearchWord, sortOption, setSort}) => {
  return (
    <div className="mb-4 d-flex justify-content-end">
      <Space wrap>
        <Search setSearchWord={setSearchWord}/>
        <Sort sortOption={sortOption} setSort={setSort}/>
      </Space>
    </div>
  )
}

export default ListHeader
