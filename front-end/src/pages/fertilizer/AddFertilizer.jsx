import fertilizerTypeArray from "src/utils/dataArray/fertilizerTypeArray"
import AddForm from "src/components/form/AddForm"

const AddFertilizer = () => {
    const initFertilizer = {
        fertilizerName: "",
        fertilizerType: fertilizerTypeArray[0],
        fertilizingPeriod: 14
    }

    const fertilizerTypeArrayForOption = fertilizerTypeArray.map((fertilizer) => {
        return(
            {
                key: fertilizer,
                value: fertilizer
            }
        )
    })

    const itemObjectArray = [
        {
            inputType: "text",
            label: "이름",
            name: "fertilizerName",
            defaultValue: "",
            required: true
        },
        {
            inputType: "select",
            label: "종류",
            name: "fertilizerType",
            defaultValue: fertilizerTypeArray[0],
            optionArray: fertilizerTypeArrayForOption
        },
        {
            inputType: "number",
            label: "주기 (일)",
            name: "fertilizingPeriod",
            defaultValue: 14,
            required: true
        }
    ]

    const requiredValueArray = ["fertilizerName", "fertilizingPeriod"];
    const isNumberArray = ["fertilizingPeriod"];

    return(
        <AddForm 
            title="비료"
            inputObject={initFertilizer}
            itemObjectArray={itemObjectArray}
            requiredValueArray={requiredValueArray}
            isNumberArray={isNumberArray}
            submitUrl="/fertilizer" />
    )
}

export default AddFertilizer;