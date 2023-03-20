import { CButton } from "@coreui/react";
import { Space } from "antd";
import { useState } from "react";
import AddWatering from "./AddWatering"

const AddWateringForm = (props) => {
    const plantNo = props.plantNo;
    const setWateringList = props.setWateringList;
    const openNotification = props.openNotification;

    const [formOpen, setFormOpen] = useState(false);

    // form 닫기 함수
    const closeForm = () => {
        setFormOpen(false);
    }

    return (
        <>
            {formOpen
                ?
                <AddWatering
                    plantNo={plantNo}
                    closeForm={closeForm}
                    openNotification={openNotification}
                    setWateringList={setWateringList} 
                    />
                :
                <Space size={[1, 5]} className="float-end">
                    <CButton color="link-secondary"><small>물주기 정보 초기화</small></CButton>
                    <CButton onClick={() => {setFormOpen(true)}} color="primary" size="sm" shape="rounded-pill">물주기</CButton>
                </Space>
            }
        </>
    )
}

export default AddWateringForm;