import {CButton} from "@coreui/react";
import {Popconfirm, Space} from "antd";
import WateringForm from "./WateringForm";
import deleteData from "../../../../api/backend-api/common/deleteData";

const HandleWateringForm = ({
                            plantNo,
                            setWateringList,
                            chemicalList,
                            isWateringFormOpen,
                            setIsWateringFormOpen,
                            setEditingKey,
                            wateringCallBack
                          }) => {
  const deleteAllWatering = async () => {
    await deleteData(`/plant/${plantNo}/`, "watering");
    setWateringList([]);
  }

  const onClickWateringFormBtn = () => {
    setIsWateringFormOpen(!isWateringFormOpen)
    setEditingKey('');
  }

  return (
    <>
      {isWateringFormOpen
        ?
        <WateringForm
          plantNo={plantNo}
          closeForm={() => {
            setIsWateringFormOpen(!isWateringFormOpen)
          }}
          chemicalList={chemicalList}
          wateringCallBack={wateringCallBack}
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
          <CButton onClick={onClickWateringFormBtn} color="primary" size="sm" shape="rounded-pill">물주기</CButton>
        </Space>
      }
    </>
  )
}

export default HandleWateringForm;
