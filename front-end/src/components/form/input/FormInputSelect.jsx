import {CInputGroup, CInputGroupText, CFormSelect} from "@coreui/react";
import {Alert} from "antd";
import AddItemButton from "src/components/button/AddItemButton";
import AddPlaceInPlantForm from "./AddPlaceInPlantForm";
import {useState} from "react";

const FormInputSelect = ({inputItem, onChange}) => {
  const [optionArray, setOptionArray] = useState(inputItem.optionArray);

  const addPlace = (place) => {
    setOptionArray([{...place}]);
    onChange({target: {name: "placeNo", value: place.key}});
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
          defaultValue={optionArray[0].key}>
          {
            optionArray.map((item) => (
              <option value={item.key} key={item.key}>{item.value}</option>
            ))
          }
        </CFormSelect>
      </CInputGroup>
    </>
  )
}

export default FormInputSelect;
