import { CInputGroup, CInputGroupText } from "@coreui/react";
import { DatePicker } from "antd";
import locale from 'antd/es/date-picker/locale/ko_KR';
import 'dayjs/locale/ko';

const FormInputDate = (props) => {
    const inputItem = props.inputItem;
    const onChange = props.onChange;

    const handleOnChange = (dateString) => {
        const data = {
            target: {
                name: props.inputItem.name,
                value: dateString
            }
        }

        onChange(data);
    }

    return (
        <CInputGroup className="mb-3 mt-3">
            <CInputGroupText id="basic-addon1">{inputItem.label}</CInputGroupText>
            <DatePicker
                style={{ width: '82%' }}
                onChange={(date, dateString) => { handleOnChange(dateString) }}
                locale={locale}
            />
        </CInputGroup>
    )
}

export default FormInputDate;