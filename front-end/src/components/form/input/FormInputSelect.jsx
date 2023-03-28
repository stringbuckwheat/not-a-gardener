import {CInputGroup, CInputGroupText, CFormSelect} from "@coreui/react";
import {Alert} from "antd";
import AddItemButton from "src/components/button/AddItemButton";

const FormInputSelect = (props) => {
  const inputItem = props.inputItem;
  const optionArray = inputItem.optionArray;

  if (optionArray.length == 0) {
    return (
      <>
        <CInputGroup>
          <CInputGroupText id="basic-addon1">{inputItem.label}</CInputGroupText>
          <CFormSelect disabled/>
        </CInputGroup>
        <Alert
          className="mt-1 mb-3"
          message="등록된 장소가 없습니다."
          type="warning"
          action={
            <AddItemButton title="장소 추가하기" size="sm" addUrl="/place/add"/>
          }
          showIcon
        />
      </>
    )
  }

  return (
    <>
      <CInputGroup className="mb-3">
        <CInputGroupText id="basic-addon1">{inputItem.label}</CInputGroupText>
        <CFormSelect
          name={inputItem.name}
          onChange={props.onChange}
          defaultValue={optionArray[0].key}>
          {
            inputItem.optionArray.map((item) => (
              <option value={item.key}>{item.value}</option>
            ))
          }
        </CFormSelect>
      </CInputGroup>
    </>
  )
}

export default FormInputSelect;
