import authAxios from "src/utils/interceptors";
import { useState } from "react";
import { CButton, CContainer } from "@coreui/react";
import 'dayjs/locale/ko';
import getFertilizerList from "src/api/backend-api/fertilizer/getFertilizerList";
import AddWatering from "./AddWatering";
import { Table, notification, Popconfirm, Space } from "antd";
import CIcon from "@coreui/icons-react";
import { cilTrash } from "@coreui/icons";
import getWateringNotificationMsg from "src/components/notification/getWateringNotificationMsg";
import getData from "src/api/backend-api/common/getData";
import AddWateringForm from "./AddWateringForm";
import deleteData from "src/api/backend-api/common/deleteData";

/**
 * plant detail 아래쪽에 table로 들어감
 * @param {*} props 
 * @returns 
 */

const WateringList = (props) => {
    const plant = props.plant;
    const wateringList = props.wateringList;
    const setWateringList = props.setWateringList;

    const wateringTableColumnArray = [
        {
            title: '언제',
            dataIndex: 'wateringDate',
            key: 'wateringDate'
        },
        {
            title: '무엇을',
            dataIndex: 'chemicalName',
            key: 'chemicalName',
        },
        {
            title: '며칠만에',
            dataIndex: 'wateringPeriod',
            key: 'wateringPeriod',
        },
        {
            title: '',
            key: 'action',
            render: (_, record) => (
                <Popconfirm
                    placement="topRight"
                    title="이 기록을 삭제하시겠습니까?"
                    description="삭제한 물 주기 정보는 되돌릴 수 없어요"
                    onConfirm={() => { confirm(record.wateringNo) }}
                    okText="네"
                    cancelText="아니요"
                >
                    <CIcon icon={cilTrash} />
                </Popconfirm>
            ),
        },
    ]

    const confirm = async (wateringNo) => {
        await deleteData("/watering", wateringNo);

        const res = await getData(`/plant/${plant.plantNo}/watering`);
        setWateringList(res);
    };

    // 물주기 추가/수정/삭제 후 메시지
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

            <CContainer>
                <div className="mt-4 mb-3">
                    <AddWateringForm
                        plantNo={plant.plantNo}
                        openNotification={openNotification}
                        wateringList={wateringList}
                        setWateringList={setWateringList}
                    />
                </div>

                <Table
                    columns={wateringTableColumnArray}
                    dataSource={wateringList.map((watering) => {
                        return ({
                            wateringNo: watering.wateringNo,
                            wateringDate: watering.wateringDate,
                            chemicalName: watering.chemicalName,
                            wateringPeriod: watering.wateringPeriod == 0 ? "" : `${watering.wateringPeriod}일만에`
                        })
                    })}
                />
            </CContainer>
        </>
    )
}

export default WateringList;