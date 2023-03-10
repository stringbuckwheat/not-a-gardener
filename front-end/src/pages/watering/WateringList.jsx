import authAxios from "src/utils/interceptors";
import { useEffect, useState } from "react";
import DefaultTable from "src/components/table/DefaultTable";
import { CButton, CRow } from "@coreui/react";
import AddWateringButton from "src/components/button/AddWateringButton";
import { CCol, CFormSelect, CCard } from "@coreui/react";
import { DatePicker } from 'antd';
import 'dayjs/locale/ko';
import locale from 'antd/es/date-picker/locale/ko_KR';
import CIcon from "@coreui/icons-react";
import { cilDrop } from "@coreui/icons";
import getFertilizerList from "src/api/backend-api/fertilizer/getFertilizerList";
import { Select } from "antd";
import AddWatering from "./AddWatering";

/**
 * plant detail 아래쪽에 table로 들어감
 * @param {*} props 
 * @returns 
 */

const WateringList = (props) => {
    const plant = props.plant;
    console.log("watering list plant", plant);
    const [wateringList, setWateringList] = useState([{}]);

    useEffect(() => {
        console.log("use effect plantNo", plant.plantNo);

        authAxios.get(`/plant/${plant.plantNo}/watering`)
            .then((res) => {
                console.log("watering list response", res);
                setWateringList(res.data);
            })
    }, [plant])

    const wateringTableColumnArray = [
        {
            title: '날짜',
            dataIndex: 'wateringDate',
            key: 'wateringDate'
        },
        {
            title: 'fertilizerName',
            dataIndex: 'fertilizerName',
            key: 'fertilizerName',
        },
        {
            title: '며칠만에',
            dataIndex: 'fertilizerName',
            key: 'fertilizerName',
        },
        {
            title: '해석',
            dataIndex: 'fertilizerName',
            key: 'fertilizerName',
        }
    ]

    const [formOpen, setFormOpen] = useState(false);

    const [fertilizerList, setFertilizerList] = useState([{}]);

    // 비료 
    const onClickFormBtn = async () => {
        const fetchFertilizerList = await getFertilizerList();
        console.log("fetchFertilizerList", fetchFertilizerList);

        const fertilizerListForSelect = fetchFertilizerList.data.map((fertilizer) => {
            return (
                {
                    value: fertilizer.fertilizerNo,
                    label: fertilizer.fertilizerName
                }
            )
        })
        console.log("map 이후", fertilizerListForSelect)

        // 맨 앞에 맹물 추가
        fertilizerListForSelect.unshift({
            value: 0,
            label: '맹물'
        })

        console.log("맹물 추가", fertilizerListForSelect);

        setFertilizerList(fertilizerListForSelect);
        setFormOpen(true);
    }

    const closeForm = () => {
        setFormOpen(false);
    }

    return (
        <>
            {formOpen
                ?
                <AddWatering plantNo={plant.plantNo} closeForm={closeForm} fertilizerList={fertilizerList}/>
                : <>
                    {/* <AddWateringButton plantNo={plant.plantNo}/> */}
                    <CButton onClick={onClickFormBtn} color="info" size={"sm"} variant="outline" shape="rounded-pill">form 열기</CButton>
                    <CButton color="info" size={"sm"} variant="outline" shape="rounded-pill">물주기 정보 초기화</CButton>
                </>}
            <DefaultTable
                path={plant.plantNo}
                columns={wateringTableColumnArray}
                list={wateringList} />
        </>

    )
}

export default WateringList;