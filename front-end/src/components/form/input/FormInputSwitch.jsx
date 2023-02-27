import { CFormSwitch } from "@coreui/react";

const FormInputSwitch = (props) => {
    const inputItem = props.inputItem;
    
    console.log("switch");
    console.log("onClick", props.onClick);
    console.log("props", props);

    return(
        <CFormSwitch 
            className="text-medium-emphasis"
            checked={props.value}
            name={inputItem.name} 
            onClick={props.onClick} 
            size="lg" 
            label={props.label} 
            id="formSwitchCheckChecked"/>
    );
}

export default FormInputSwitch;