import { CInputGroup, CInputGroupText, CFormInput } from "@coreui/react";

const FormInputText = (props) => {
    const inputItem = props.inputItem;

    return(
        <CInputGroup className="mb-3 mt-3">
            <CInputGroupText id="basic-addon1">{inputItem.label}</CInputGroupText>
            <CFormInput
                defaultValue={inputItem.defaultValue}
                aria-describedby="basic-addon1"
                name={inputItem.name}
                required={inputItem.required}
                onChange={props.onChange}
                feedbackInvalid={props.feedbackInvalid}
            />
        </CInputGroup>
    )
}

export default FormInputText;