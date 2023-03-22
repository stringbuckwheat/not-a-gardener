import {CButton} from "@coreui/react"
import {useNavigate} from "react-router-dom"
import getPlaceListForSelect from "src/api/service/getPlaceListForSelect";

const AddPlantButton = (props) => {
  const size = props.size;
  const className = props.className;
  const navigate = useNavigate();

  const onClick = async () => {
    const placeList = await getPlaceListForSelect("/place");
    navigate("/plant/add", {state: placeList});
  }

  return (
    <CButton className={className} onClick={onClick} color="success" size={size} variant="outline" shape="rounded-pill">식물 추가하기</CButton>
  )
}

export default AddPlantButton
