import {Select} from "antd";

const Sort = (props) => {
  const sortOption = props.sortOption;
  const setSort = props.setSort;

  const onChange = (value) => {
    setSort(value);
  }

  return (
    <Select
      className="width-130"
      defaultValue={sortOption[0].value}
      onChange={onChange}
      options={sortOption}
    />
  )
}

export default Sort;
