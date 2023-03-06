import ModifyForm from "src/components/form/ModifyForm";
import getPlaceInputItemArray from "src/utils/function/getPlaceInputItemArray";

const ModifyPlace = (props) => {
    const title = props.title;
    const onClickGetBackBtn = props.onClickGetBackBtn;
    const place = props.place;
    const itemObjectArray = getPlaceInputItemArray(place);
    const requiredValueArray = ["placeName"];

    return(
        <ModifyForm
            title={title}
            inputObject={place}
            itemObjectArray={itemObjectArray}
            requiredValueArray={requiredValueArray}
            isNumberArray={[]}
            submitUrl="place"
            path={place.placeNo}
            onClickGetBackBtn={onClickGetBackBtn}
        />

    )
}

export default ModifyPlace;