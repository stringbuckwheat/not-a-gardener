import {CInputGroup, CInputGroupText, CFormSelect} from "@coreui/react";
import AddPlaceInPlantForm from "./AddPlaceInPlantForm";
import {useState} from "react";

const InputSelect = ({inputItem, onChange}) => {
  const [optionArray, setOptionArray] = useState(inputItem.optionArray);

  const addPlace = (place) => {
    setOptionArray([{...place}]);
    onChange({target: {name: "placeId", value: place.key}});
  }

  if (optionArray.length == 0) {
    return <AddPlaceInPlantForm addPlace={addPlace}/>
  }

  return (
    <>
      <CInputGroup className="mb-3">
        <CInputGroupText id="basic-addon1">{inputItem.label}</CInputGroupText>
        <CFormSelect
          name={inputItem.name}
          onChange={onChange}
          defaultValue={inputItem.defaultValue}>
          {
            optionArray.map((item, index) => (
              <option value={item.key} key={index}>{item.value}</option>
            ))
          }
        </CFormSelect>
      </CInputGroup>
    </>
  )
}

export default InputSelect;
