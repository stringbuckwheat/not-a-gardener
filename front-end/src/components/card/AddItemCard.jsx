import {CCol, CWidgetStatsF} from "@coreui/react";
import {cilPlus} from "@coreui/icons";
import CIcon from '@coreui/icons-react';

const AddItemCard = ({addMsg, onClick}) => {

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
