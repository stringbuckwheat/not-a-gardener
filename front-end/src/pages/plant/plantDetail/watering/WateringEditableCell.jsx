import {DatePicker, Form, Select} from "antd";
import locale from 'antd/es/date-picker/locale/ko_KR';
import getDisabledDate from "../../../../utils/function/getDisabledDate";

const WateringEditableCell = ({
                                editWatering,
                                setEditWatering,
                                editing,
                                dataIndex,
                                title,
                                inputType,
                                record,
                                index,
                                children,
                                chemicalList,
                                ...restProps
                              }) => {

  const inputNode = inputType === 'date'
    ? <DatePicker
      locale={locale}
      disabledDate={getDisabledDate}
      onChange={(date, dateString) => setEditWatering({...editWatering, wateringDate: dateString})}/>
    : <Select
      options={chemicalList}
      onChange={(value) => setEditWatering({...editWatering, chemicalId: value})}/>;

  return (
    <td {...restProps}>
      {editing
        ? (
          <Form.Item
            name={dataIndex}
            style={{margin: 0}}
            rules={[{required: true, message: "날짜를 입력해주세요",},]}
          >
            {inputNode}
          </Form.Item>
        ) : (
          children
        )}
    </td>
  );
}

export default WateringEditableCell;
