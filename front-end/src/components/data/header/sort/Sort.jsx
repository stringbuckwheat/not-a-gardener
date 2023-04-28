import {Select} from "antd";

/**
 * 정렬 select
 * @param sortOption
 * @param setSort
 * @returns {JSX.Element}
 * @constructor
 */
const Sort = ({sortOption, setSort}) => {
  return (
    <Select
      className="width-130"
      defaultValue={sortOption[0].value}
      onChange={(value) => setSort(value)}
      options={sortOption}
    />
  )
}

export default Sort;
