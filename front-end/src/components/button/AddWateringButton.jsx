import { Link } from "react-router-dom";
import { CButton } from "@coreui/react";

const AddWateringButton = (props) => {

    const watering = {
        plantNo: props.plantNo,
        fertilizerNo: 0,
        wateringDate: {}
    }

    return (
        <Link to="/watering/add" state={watering}>
            <CButton color="info" size={"sm"} variant="outline" shape="rounded-pill">물주기</CButton>
        </Link>
    )
}

export default AddWateringButton;