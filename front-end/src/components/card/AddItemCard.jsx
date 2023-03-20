import { CCol, CWidgetStatsF } from "@coreui/react";
import { Link } from "react-router-dom";
import { cilPlus } from "@coreui/icons";
import CIcon from '@coreui/icons-react';


const AddItemCard = (props) => {
    const addUrl = props.addUrl;
    const addMsg = props.addMsg;

    return (
        <CCol md={3} xs={12}>
            <Link to={addUrl} style={{ textDecoration: "none" }}>
                <CWidgetStatsF
                    className="mb-3"
                    color="dark"
                    icon={<CIcon icon={cilPlus} height={30} />}
                    value={addMsg}
                />
            </Link>
        </CCol>
    )
}

export default AddItemCard