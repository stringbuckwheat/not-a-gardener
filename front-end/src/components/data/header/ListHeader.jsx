import {Space} from "antd";
import Search from "./search/Search";
import Sort from "./sort/Sort";

const ListHeader = (props) => {
  const setSearchWord = props.setSearchWord;
  const sortOption = props.sortOption;
  const setSort = props.setSort;

  return (
    <div className="mb-4 d-flex justify-content-end">
      <Space wrap>
        <Search
          setSearchWord={setSearchWord}/>
        <Sort
          sortOption={sortOption}
          setSort={setSort}/>
      </Space>
    </div>
  )
}

export default ListHeader
