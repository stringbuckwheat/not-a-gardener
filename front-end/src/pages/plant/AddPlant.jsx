import {useState} from "react";
import ItemForm from "src/components/form/ItemForm";
import SubmitForAddButton from "src/components/button/SubmitForAddButton";
import {useLocation} from "react-router-dom";
import getPlantFormArray from "../../utils/function/getPlantFormArray";

const AddPlant = (props) => {
  const state = useLocation().state;
  const placeList = props.placeList ? props.placeList : state;
  const {callBackFunction} = props;

  const initPlant = {
    plantName: "",
    plantSpecies: "",
    placeNo: !placeList.length == 0 ? placeList[0].key : 0,
    medium: "흙과 화분",
    earlyWateringPeriod: 0,
    birthday: ""
  }

  const [plant, setPlant] = useState(initPlant);

  const onChange = (e) => {
    const {name, value} = e.target;
    setPlant(setPlant => ({...plant, [name]: value}));
    console.log("onchange", plant);
  }

  const validation = plant.plantName != '' && placeList.length !== 0;

  return (
    <ItemForm
      title="식물 추가"
      inputObject={plant}
      itemObjectArray={getPlantFormArray(placeList)}
      onChange={onChange}
      submitBtn={<SubmitForAddButton
        url="/plant"
        callBackFunction={callBackFunction}
        data={plant}
        validation={validation}/>}/>
  )
}

export default AddPlant;
