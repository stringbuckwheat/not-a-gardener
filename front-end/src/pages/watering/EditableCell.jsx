import {DatePicker, Form, Select} from "antd";
import locale from 'antd/es/date-picker/locale/ko_KR';
import getDisabledDate from "../../utils/function/getDisabledDate";
import {useEffect, useState} from "react";
import getChemicalListForSelect from "../../api/service/getChemicalListForSelect";

const EditableCell = (props) => {
    const { editWatering, setEditWatering, editing, dataIndex, title, inputType, record, index, children, ...restProps } = props;

    const [chemicalList, setChemicalList] = useState([]);

    useEffect(() => {
      getChemicalListForSelect(setChemicalList);
    }, [])

    const inputNode = inputType === 'date'
        ? <DatePicker
            locale={locale}
            disabledDate={getDisabledDate}
            onChange={(date, dateString) => { setEditWatering({ ...editWatering, wateringDate: dateString }) }} />
        : <Select
            options={chemicalList}
            onChange={(value) => { setEditWatering({ ...editWatering, chemicalNo: value }) }} />;

    return (
        <td {...restProps}>
            {editing
                ? (
                    <Form.Item
                        name={dataIndex}
                        style={{ margin: 0 }}
                        rules={[{ required: true, message: "날짜를 입력해주세요", },]}
                    >
                        {inputNode}
                    </Form.Item>
                ) : (
                    children
                )}
        </td>
    );
}

export default EditableCell;
