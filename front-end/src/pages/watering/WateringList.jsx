import authAxios from "src/utils/interceptors";
import { useState } from "react";
import { CButton } from "@coreui/react";
import 'dayjs/locale/ko';
import getFertilizerList from "src/api/backend-api/fertilizer/getFertilizerList";
import AddWatering from "./AddWatering";
import { Table, notification, Popconfirm } from "antd";
import CIcon from "@coreui/icons-react";
import { cilTrash } from "@coreui/icons";
import getWateringNotificationMsg from "src/components/notification/getWateringNotificationMsg";

/**
 * plant detail 아래쪽에 table로 들어감
 * @param {*} props 
 * @returns 
 */

const WateringList = (props) => {
    const plant = props.plant;
    const wateringList = props.wateringList;
    const setWateringList = props.setWateringList;

    // 오늘 물 줬는지
    // ==로 비교 불가능
    let isPlantWateredToday = false;

    if (wateringList.length > 0) {
        isPlantWateredToday = new Date() >= new Date(wateringList[0].wateringDate);
    }

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
        },
        {
            title: 'Action',
            key: 'action',
            render: (_, record) => (
                <Popconfirm
                    placement="topRight"
                    title={deleteText}
                    description={description}
                    onConfirm={() => {confirm(record.wateringNo)}}
                    okText="네"
                    cancelText="아니요"
                >
                    <CIcon icon={cilTrash} />
                </Popconfirm>
            ),
        },
    ]

    const deleteText = "이 기록을 삭제하시겠습니까?"
    const description = "삭제한 물 주기 정보는 되돌릴 수 없어요"

    const confirm = (wateringNo) => {
        console.log("삭제 클릭");
        console.log("wateringNo", wateringNo);

        authAxios.delete(`/watering/${wateringNo}`)
        .then(() => {
            // watering list 바꾸기
            authAxios.get(`/plant/${plant.plantNo}/watering`)
            .then((res) => {
                setWateringList(res.data);
            })
        })
    };

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

    const [api, contextHolder] = notification.useNotification();
    const openNotification = (wateringMsg) => {
        const msg = getWateringNotificationMsg(wateringMsg.wateringCode)
    
        api.open({
          message: msg.title,
          description: msg.content,
          duration: 4,
        });
      };

    return (
        <>
            {contextHolder}

            {formOpen && !isPlantWateredToday
                ?
                <AddWatering
                    plantNo={plant.plantNo}
                    closeForm={closeForm}
                    fertilizerList={fertilizerList}
                    openNotification={openNotification}
                    wateringList={wateringList}
                    setWateringList={props.setWateringList} />
                : <>
                    {!isPlantWateredToday ?
                        <CButton onClick={onClickFormBtn} color="info" size={"sm"} variant="outline" shape="rounded-pill">form 열기</CButton>
                        : <></>
                    }
                    <CButton color="info" size={"sm"} variant="outline" shape="rounded-pill">물주기 정보 초기화</CButton>
                </>}

            <Table
                className="mt-3"
                columns={wateringTableColumnArray}
                dataSource={wateringList.map((watering) => {
                    return ({
                        wateringNo: watering.wateringNo,
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