import { Link } from "react-router-dom";
import { CButton } from "@coreui/react";

const AddItemButton = (props) => {
    const addUrl = props.addUrl;
    const size = props.size;
    const title = props.title;

    return (
        <Link to={addUrl}>
            <CButton color="success" size={size} variant="outline" shape="rounded-pill">{title}</CButton>
        </Link>
    )
}

export default AddItemButton;