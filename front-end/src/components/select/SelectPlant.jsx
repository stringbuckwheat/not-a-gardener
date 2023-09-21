import {Select} from "antd";
import {useSelector} from "react-redux";

const SelectPlant = ({onChange, onSearch, className, size}) => {
  const plants = useSelector(state => state.plants).map((plant) => (
    {
      label: `${plant.name} (${plant.name})`,
      value: plant.id,
    }
  ));

  return (
    <Select
      name="plantId"
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
      options={plants}
    />
  )
}

export default SelectPlant
