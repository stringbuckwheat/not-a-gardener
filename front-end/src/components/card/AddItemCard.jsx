import {CCol, CWidgetStatsF} from "@coreui/react";
import {Link} from "react-router-dom";
import {cilPlus} from "@coreui/icons";
import CIcon from '@coreui/icons-react';


const AddItemCard = ({addUrl, addMsg, onClick}) => {

  return (
    <CCol md={3} xs={12}>
      <CWidgetStatsF
        onClick={onClick}
        className="mb-3"
        color="dark"
        icon={<CIcon icon={cilPlus} height={30}/>}
        value={addMsg}
      />
    </CCol>
  )
}

export default AddItemCard
