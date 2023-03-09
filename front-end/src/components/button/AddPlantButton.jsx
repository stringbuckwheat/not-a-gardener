import { CButton } from "@coreui/react"
import getPlaceList from "src/utils/function/getPlaceList"
import { useNavigate } from "react-router-dom"

const AddPlantButton = (props) => {
    const navigate = useNavigate();

    const onClick = async () => {
      const placeList = await getPlaceList()

      navigate("/plant/add", { state: placeList });
      console.log("navigate");
    }

    return (
        <CButton onClick={onClick} color="success" size={props.size} variant="outline" shape="rounded-pill">식물 추가하기</CButton>
    )
}

export default AddPlantButton