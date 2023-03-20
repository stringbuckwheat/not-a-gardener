import getPlaceInputItemArray from "src/utils/function/getPlaceInputItemArray";
import ItemForm from "src/components/form/ItemForm";
import { useState } from "react";
import SubmitForAddButton from "src/components/button/SubmitForAddButton";

const AddPlace = () => {
    const [ place, setPlace ] = useState({
        placeName: "",
        option: "실내",
        artificialLight: "미사용"
    });

    const onChange = (e) => {
        const { name, value } = e.target;
        setPlace(setPlace => ({ ...place, [name]: value }));
        console.log("onchange", place);
    }

    const itemObjectArray = getPlaceInputItemArray(place);

    const validation = place.placeName !== "";

    return(
        <ItemForm
            title="장소 추가"
            inputObject={place}
            itemObjectArray={itemObjectArray}
            onChange={onChange} 
            submitBtn={<SubmitForAddButton 
                            url="/place"
                            data={place}
                            validation={validation}/>}/>
    )
}

export default AddPlace;