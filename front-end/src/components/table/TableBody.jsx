import {CTableRow,CNavLink, CTableHeaderCell, CTableDataCell, CLink, CTableBody} from "@coreui/react";
import { Link, useNavigate } from "react-router-dom";
const TableBody = (props) => {
    const list = props.list;

    const navigate = useNavigate();

    const onClick = () => {
        navigate('/plant')
    }

    return (
        <CTableBody>
        {list.map((data, idx) => {
            const url = "/plant/" + data.plantNo;

            return(
                <CTableRow onClick={() => navigate(url, {state: data})}>
                    <CTableDataCell >{data.plantName}</CTableDataCell>
                    <CTableDataCell>{data.placeName}</CTableDataCell>
                    <CTableDataCell>{data.medium}</CTableDataCell>
                    <CTableDataCell>{data.plantSpecies}</CTableDataCell>
                    <CTableDataCell>{data.createDate}</CTableDataCell>
                </CTableRow>
            )
        })} 
        </CTableBody>
    )
}

export default TableBody;