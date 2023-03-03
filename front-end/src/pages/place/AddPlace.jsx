import placeOptionObjectArray from "src/utils/dataArray/placeOptionArray";
import artificialLightArray from "src/utils/dataArray/artificialLightArray";
import AddForm from "src/components/form/AddForm";

const AddPlace = () => {
    const initPlace = {
        placeName: "",
        option: "실내",
        artificialLight: "미사용"
    }

    const itemObjectArray = [
        {
            inputType: "text",
            label: "장소 이름",
            name: "placeName",
            defaultValue: initPlace.placeName,
            required: true
        },
        {
            inputType: "select",
            label: "이 장소의 위치",
            name: "option",
            defaultValue: initPlace.option,
            optionArray: placeOptionObjectArray
        },
        {
            inputType: "select",
            label: "이 장소는 식물등을",
            name: "artificialLight",
            defaultValue: initPlace.artificialLight,
            optionArray: artificialLightArray
        }
    ]

    const requiredValueArray = ["placeName"];

    return(
        <AddForm 
            title="장소"
            inputObject={initPlace}
            itemObjectArray={itemObjectArray}
            requiredValueArray={requiredValueArray}
            isNumberArray={[]}
            submitUrl="/place" />
    )
}

export default AddPlace;