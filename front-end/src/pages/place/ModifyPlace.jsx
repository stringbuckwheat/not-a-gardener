import { useState } from "react";
import ModifyFormButtons from "src/components/button/ModifyFormButtons";
import ItemForm from "src/components/form/ItemForm";
import getPlaceInputItemArray from "src/utils/function/getPlaceInputItemArray";

const ModifyPlace = (props) => {
    const changeModifyState = props.changeModifyState;

    const [place, setPlace] = useState(props.place);

    const itemObjectArray = getPlaceInputItemArray(place);

    const onChange = (e) => {
        const { name, value } = e.target;
        setPlace(setPlace => ({ ...place, [name]: value }));
        console.log("onchange", place);
    }

    const validation = place.placeName !== "";

    return (
        <ItemForm
            title="장소 수정"
            inputObject={place}
            itemObjectArray={itemObjectArray}
            onChange={onChange}
            submitBtn={<ModifyFormButtons
                data={place}
                url="place"
                path={place.placeNo}
                changeModifyState={changeModifyState}
                validation={validation}
            />
            }
        />
    )
}

export default ModifyPlace;