import { CTableHead, CTableRow, CTableHeaderCell } from "@coreui/react";
const TableHead = (props) => {
    const item = props.item;

    return(
        <CTableHead>
            <CTableRow>
                {item.map((cell) => {
                    return(
                        <CTableHeaderCell scope="col">{cell}</CTableHeaderCell>
                    )
                })}
            </CTableRow>
        </CTableHead>
    )
}

export default TableHead;