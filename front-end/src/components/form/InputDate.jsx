import {DatePicker, Form} from "antd";
import locale from 'antd/es/date-picker/locale/ko_KR';
import 'dayjs/locale/ko';

/**
 * FormInputHandler에서 쓸 date 입력 Input 컴포넌트
 * @param inputItem
 * @param onChange
 * @returns {JSX.Element}
 * @constructor
 */
const InputDate = ({inputItem, onChange}) => {
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
    <>
      <Form.Item
        label={inputItem.label}
        name={inputItem.name}
        rules={[{required: inputItem.required, message: '날짜를 입력해주세요'}]}
      >
        <DatePicker
          className="width-full"
          placeholder={inputItem.required ? "날짜 선택" : "모르겠다면 비워둬도 좋아요"}
          onChange={(date, dateString) => {
            handleOnChange(dateString)
          }}
          locale={locale}
        />
      </Form.Item>
    </>
  )
}

export default InputDate;
