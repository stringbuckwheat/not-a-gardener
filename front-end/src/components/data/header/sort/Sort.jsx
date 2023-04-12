import {Select} from "antd";

const Sort = ({sortOption, setSort}) => {
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
