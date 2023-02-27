import { CInputGroup, CInputGroupText, CFormSelect } from "@coreui/react";

const FormInputSelect = (props) => {
    const inputItem = props.inputItem;

    return(
        <CInputGroup className="mb-3">
            <CInputGroupText id="basic-addon1">{inputItem.label}</CInputGroupText>
            <CFormSelect name={inputItem.name} onChange={props.onChange} defaultValue={inputItem.defaultValue}>
                {
                    inputItem.optionArray.map((option, idx) => {
                        return(
                            <option value={option}>{option}</option>
                        )     
                    })
                }
            </CFormSelect>
        </CInputGroup>
    )
}

export default FormInputSelect;