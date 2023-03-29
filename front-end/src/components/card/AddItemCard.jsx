import {CCol, CWidgetStatsF} from "@coreui/react";
import {Link} from "react-router-dom";
import {cilPlus} from "@coreui/icons";
import CIcon from '@coreui/icons-react';


const AddItemCard = (props) => {
  const {addUrl, addMsg} = props;

  return (
    <CCol md={3} xs={12}>
      <Link
        to={addUrl}
        className="no-text-decoration">
        <CWidgetStatsF
          className="mb-3"
          color="dark"
          icon={<CIcon icon={cilPlus} height={30}/>}
          value={addMsg}
        />
      </Link>
    </CCol>
  )
}

export default AddItemCard
