import {Link} from "react-router-dom";
import {CButton} from "@coreui/react";

const AddItemButton = ({addUrl, size, title}) => {
  return (
    <Link to={addUrl}>
      <CButton color="success" size={size} variant="outline" shape="rounded-pill">{title}</CButton>
    </Link>
  )
}

export default AddItemButton;
