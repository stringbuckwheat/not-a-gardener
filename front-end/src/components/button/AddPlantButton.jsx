import {CButton} from "@coreui/react"
import {useNavigate} from "react-router-dom"
import getPlaceListForSelect from "src/api/service/getPlaceListForSelect";

const AddPlantButton = (props) => {
  const {size, className, shape} = props;
  const navigate = useNavigate();

  const onClickAddPlant = async () => {
    const placeList = await getPlaceListForSelect();
    navigate("/plant/add", {state: placeList});
  }

  return (
      <CButton className={className} onClick={onClickAddPlant} color="success" size={size} variant="outline" shape={shape ? "" : "rounded-pill"}>식물
        추가하기
      </CButton>
  )
}

export default AddPlantButton
