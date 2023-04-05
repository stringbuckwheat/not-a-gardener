import {Select} from "antd";

const SelectPlant = ({onChange, onSearch, plantList, className, size}) => {

  return (
    <Select
      name="plantNo"
      showSearch
      placeholder="식물 이름을 입력해주세요"
      optionFilterProp="children"
      onChange={onChange}
      onSearch={onSearch}
      className={className}
      size={size}
      filterOption={(input, option) =>
        (option?.label ?? '').includes(input)
      }
      options={plantList}
    />
  )
}

export default SelectPlant
