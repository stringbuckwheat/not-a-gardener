import { useLocation } from 'react-router-dom'
import DefaultForm from 'src/components/form/DefaultForm'

const PlaceDetail = () => {
    
    // props
    const { state } = useLocation();

    const isNew = state.placeNo === undefined;

    const title = "장소";
    const action = isNew ? "추가" : "수정";

    const itemObjectArray = [
      {
        inputType: "text",
        label: "장소이름",
        name: "placeName",
        defaultValue: state.placeName,
        required: true
      },
      {
        inputType: "select",
        label: "이 장소의 위치",
        name: "option",
        defaultValue: state.option,
        optionArray:["실내", "베란다", "야외"]
      },
      {
        inputType: "switch",
        name:"artificialLight",
        defaultValue: state.artificialLight,
      }
    ];

    return(
      <DefaultForm 
        title={title} 
        action={action}
        inputObject={state}
        itemObjectArray={itemObjectArray} 
        isNew={isNew}
        path={isNew ? undefined : state.placeNo}
        submitUrl="/place" />
    );
}

export default PlaceDetail;