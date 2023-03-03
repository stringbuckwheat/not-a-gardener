import {CTableRow, CTableDataCell, CTableBody} from "@coreui/react";
import { useNavigate } from "react-router-dom";

const PlantTableBody = (props) => {
    const list = props.list;
    console.log("table body list", list);
    const keySet = props.keySet;

    const navigate = useNavigate();
    const onClick = (url, data) => {
        navigate(url, {state: data});
    }

    return (
        <CTableBody>
        {list.map((data, idx) => {
            const url = props.linkUrl + data.plantNo;

            return(
                <CTableRow onClick={() => onClick(url, data)}>
                    {keySet.map((cellName) => {
                        return(
                            <CTableDataCell>{data[cellName]}</CTableDataCell>
                        )
                    })}
                </CTableRow>
            )
        })} 
        </CTableBody>
    )
}

export default PlantTableBody;