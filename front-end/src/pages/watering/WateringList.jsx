import authAxios from "src/utils/interceptors";
import { useEffect, useState } from "react";
import DefaultTable from "src/components/table/DefaultTable";
import { CButton } from "@coreui/react";
import 'dayjs/locale/ko';
import getFertilizerList from "src/api/backend-api/fertilizer/getFertilizerList";
import AddWatering from "./AddWatering";
import { Table } from "antd";

/**
 * plant detail 아래쪽에 table로 들어감
 * @param {*} props 
 * @returns 
 */

const WateringList = (props) => {
    const plant = props.plant;
    const wateringList = props.wateringList;

    const wateringTableColumnArray = [
        {
            title: '언제',
            dataIndex: 'wateringDate',
            key: 'wateringDate'
        },
        {
            title: '무엇을',
            dataIndex: 'fertilizerName',
            key: 'fertilizerName',
        },
        {
            title: '며칠만에',
            dataIndex: 'wateringPeriod',
            key: 'wateringPeriod',
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

        const fertilizerListForSelect = fetchFertilizerList.data.map((fertilizer) => {
            return (
                {
                    value: fertilizer.fertilizerNo,
                    label: fertilizer.fertilizerName
                }
            )
        })

        // 맨 앞에 맹물 추가
        fertilizerListForSelect.unshift({
            value: 0,
            label: '맹물'
        })

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
                <AddWatering plantNo={plant.plantNo} closeForm={closeForm} fertilizerList={fertilizerList} />
                : <>
                    {/* <AddWateringButton plantNo={plant.plantNo}/> */}
                    <CButton onClick={onClickFormBtn} color="info" size={"sm"} variant="outline" shape="rounded-pill">form 열기</CButton>
                    <CButton color="info" size={"sm"} variant="outline" shape="rounded-pill">물주기 정보 초기화</CButton>
                </>}

            <Table
                className="mt-3"
                columns={wateringTableColumnArray}
                dataSource={wateringList.map((watering) => {
                    return ({
                        wateringDate: watering.wateringDate,
                        fertilizerName: watering.fertilizerName,
                        wateringPeriod: watering.wateringPeriod == 0 ? "" : `${watering.wateringPeriod}일만에`
                    })
                })}
            />
        </>

    )
}

export default WateringList;