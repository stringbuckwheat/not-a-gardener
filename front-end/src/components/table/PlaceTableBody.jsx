import {CTableRow, CTableDataCell, CTableBody} from "@coreui/react";
import { useNavigate } from "react-router-dom";
import authAxios from "src/utils/interceptors";

const PlaceTableBody = (props) => {
    const list = props.list;
    console.log("table body list", list);
    const keySet = props.keySet;

    const navigate = useNavigate();
    const onClick = (url, data) => {
        authAxios.get("/place")
        .then((res) => {
            console.log("onclick res", res.data);

            const placeList = [];
  
            res.data.map((item) => {
                placeList.push({
                    key: item.placeNo,
                    value: item.placeName
                })
            })

            navigate(url, {state: {data: data, placeList: placeList}});
        })
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

export default PlaceTableBody;