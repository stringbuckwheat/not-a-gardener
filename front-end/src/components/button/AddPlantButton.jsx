import { CButton } from "@coreui/react"
import getPlaceList from "src/utils/getPlaceList"
import { useNavigate } from "react-router-dom"

const AddPlantButton = (props) => {
    const navigate = useNavigate();

    const onClick = async () => {
      const placeList = await getPlaceList()

      const state = {
        placeList: placeList,
        initPlant: {
          plantName: "",
          plantSpecies: "",
          placeNo: placeList[0].key,
          medium: "흙과 화분",
          earlyWateringPeriod: 5
        }
      }

      navigate("/plant/add", { state: state });
    }

    return (
        <CButton onClick={onClick} color="success" size={props.size} variant="outline" shape="rounded-pill">식물 추가하기</CButton>
    )
}

export default AddPlantButton