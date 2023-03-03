import { CButton } from "@coreui/react"
import { Link } from "react-router-dom"

const AddPesticideButton = (props) => {
    return (
      <Link to="/pesticide/add">
        <CButton color="success" size={props.size} variant="outline" shape="rounded-pill">살충/살균제 추가하기</CButton>
      </Link>
    )
}

export default AddPesticideButton