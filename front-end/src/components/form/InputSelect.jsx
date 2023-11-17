import {useState} from "react";
import {Form, Select} from "antd";
import AddPlaceInPlantForm from "./input/AddPlaceInPlantForm";

const InputSelect = ({inputItem, onChange}) => {
  const [optionArray, setOptionArray] = useState(inputItem.optionArray);

  const addPlace = (place) => {
    setOptionArray([{...place}]);
    onChange({target: {name: "placeId", value: place.key}});
  }

  if (optionArray.length == 0) {
    return <AddPlaceInPlantForm addPlace={addPlace}/>
  }

  const handleOnChange = (value) => {
    const data = {
      target: {
        name: inputItem.name,
        value: value
      }
    }

    onChange(data);
  }

  return (
    <Form.Item
      label={inputItem.label}
      name={inputItem.name}
      rules={[{required: inputItem.required}]}
    >
      <Select
        name={inputItem.name}
        onChange={handleOnChange}
        defaultValue={inputItem.defaultValue ? inputItem.defaultValue : {
          value: optionArray[0].key,
          label: optionArray[0].value
        }}
      >
        {
          optionArray.map((item, index) => (
            <Option value={item.key} key={index}>{item.value}</Option>
          ))
        }
      </Select>
    </Form.Item>
  )
}

export default InputSelect;
