import { CButton } from "@coreui/react";
import { Popconfirm, Space } from "antd";
import { useState } from "react";
import authAxios from "src/utils/interceptors";
import WateringForm from "./WateringForm";


const WateringFormOpen = (props) => {
    const plantNo = props.plantNo;
    const setWateringList = props.setWateringList;
    const openNotification = props.openNotification;

    const [formOpen, setFormOpen] = useState(false);

    // form 닫기 함수
    const closeForm = () => {
        setFormOpen(false);
    }

    const deleteAllWatering = () => {
        authAxios.delete(`/watering/plant/${plantNo}`);
        setWateringList([]);
    }

    return (
        <>
            {formOpen
                ?
                <WateringForm
                    plantNo={plantNo}
                    closeForm={closeForm}
                    openNotification={openNotification}
                    setWateringList={setWateringList}
                />
                :
                <Space size={[1, 5]} className="float-end">
                    <Popconfirm
                        placement="topRight"
                        title="물주기 기록을 모두 지웁니다"
                        description="삭제한 물주기 기록은 복구할 수 없어요"
                        onConfirm={deleteAllWatering}
                        okText="네"
                        cancelText="아니요"
                    >
                        <CButton color="link-secondary"><small>물주기 기록 전체 삭제</small></CButton>
                    </Popconfirm>
                    <CButton onClick={() => { setFormOpen(true) }} color="primary" size="sm" shape="rounded-pill">물주기</CButton>
                </Space>
            }
        </>
    )
}

export default WateringFormOpen;
