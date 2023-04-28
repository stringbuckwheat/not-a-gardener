import {CInputGroup, CInputGroupText} from "@coreui/react";
import {DatePicker} from "antd";
import locale from 'antd/es/date-picker/locale/ko_KR';
import 'dayjs/locale/ko';

/**
 * FormInputHandler에서 쓸 date 입력 Input 컴포넌트
 * @param inputItem
 * @param onChange
 * @returns {JSX.Element}
 * @constructor
 */
const FormInputDate = ({inputItem, onChange}) => {
  const handleOnChange = (dateString) => {
    const data = {
      target: {
        name: inputItem.name,
        value: dateString
      }
    }

    onChange(data);
  }

  return (
    <CInputGroup className="mb-3 mt-3">
      <CInputGroupText id="basic-addon1">{inputItem.label}</CInputGroupText>
      <DatePicker
        className="width-82p"
        onChange={(date, dateString) => {handleOnChange(dateString)}}
        locale={locale}
      />
      <span className="text-success">
        <small>{inputItem.required ? "" : "모르겠다면 비워둬도 좋아요"}</small>
      </span>
    </CInputGroup>
  )
}

export default FormInputDate;
