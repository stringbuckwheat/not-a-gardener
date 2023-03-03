import AddForm from "src/components/form/AddForm"

const AddPesticide = () => {
    const initPesticide = {
        pesticideName: "",
        pesticideType: "살충제",
        pesticidePeriod: 180
    }

    const optionArray = [
        {key: "살충제", value: "살충제"},
        {key: "살균제", value: "살균제"},
        {key: "천적 방제", value: "천적 방제"}
    ]

    const itemObjectArray = [
        {
            inputType: "text",
            label: "이름",
            name: "pesticideName",
            defaultValue: "",
            required: true
        },
        {
            inputType: "select",
            label: "종류",
            name: "pesticideType",
            defaultValue: "살충제",
            optionArray: optionArray
        },
        {
            inputType: "number",
            label: "주기 (일)",
            name: "pesticidePeriod",
            defaultValue: 180,
            required: true
        }
    ]

    const requiredValueArray = ["pesticideName", "pesticidePeriod"];
    const isNumberArray = ["pesticidePeriod"];

    return(
        <AddForm 
            title="살충/살균제"
            inputObject={initPesticide}
            itemObjectArray={itemObjectArray}
            requiredValueArray={requiredValueArray}
            isNumberArray={isNumberArray}
            submitUrl="/pesticide" />
    )
}

export default AddPesticide;