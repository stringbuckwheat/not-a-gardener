import getPlaceInputItemArray from "src/utils/function/getPlaceInputItemArray";
import AddForm from "src/components/form/AddForm";

const AddPlace = () => {
    const initPlace = {
        placeName: "",
        option: "실내",
        artificialLight: "미사용"
    }

    const itemObjectArray = getPlaceInputItemArray(initPlace);
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