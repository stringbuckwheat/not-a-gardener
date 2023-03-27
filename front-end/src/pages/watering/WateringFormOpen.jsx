import {CButton} from "@coreui/react";
import {Popconfirm, Space} from "antd";
import {useState} from "react";
import authAxios from "src/utils/interceptors";
import WateringForm from "./WateringForm";


const WateringFormOpen = (props) => {
  const {
    plantNo,
    setWateringList,
    openNotification,
    chemicalList,
    setPlant,
    isWateringFormOpen,
    setIsWateringFormOpen,
    setEditingKey
  } = props;

  const deleteAllWatering = () => {
    authAxios.delete(`/watering/plant/${plantNo}`);
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
          openNotification={openNotification}
          setWateringList={setWateringList}
          chemicalList={chemicalList}
          setPlant={setPlant}
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

export default WateringFormOpen;
