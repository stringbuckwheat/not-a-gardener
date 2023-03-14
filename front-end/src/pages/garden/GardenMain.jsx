import React, { useState, useEffect } from 'react'
import { CContainer, CRow } from '@coreui/react';
import NoItem from 'src/components/NoItem';
import AddPlantButton from 'src/components/button/AddPlantButton';
import GardenCard from 'src/components/card/GardenCard';
import { notification } from 'antd'
import getWateringNotificationMsg from 'src/components/notification/getWateringNotificationMsg';
import getGarden from 'src/api/backend-api/garden/getGarden';
import getFertilizerListForAddWatering from 'src/api/service/getFertilizerListForAddWatering';

const GardenMain = () => {
  const [hasPlantList, setHasPlantList] = useState(false);

  // 식물 리스트
  const [plantList, setPlantList] = useState([{
    plantNo: ''
    , plantName: ''
    , averageWateringPeriod: ''
    , wateringCode: ''
    , fertilizingCode: ''
  }]);

  // 비료 리스트
  const [fertilizerList, setFertilizerList] = useState([{
    fertilizerNo: 0,
    fertilizerName: ""
  }])

  // 컴포넌트 마운트 시점에 받아올 데이터
  const onMount = async () => {
    const gardenList = await getGarden();

    if (gardenList.length !== 0) {
      setHasPlantList(true);
      setPlantList(gardenList);
    }

    const fertilizerList = await getFertilizerListForAddWatering();
    setFertilizerList(fertilizerList);
  }

  // 백엔드에서 식물 리스트를 받아온다
  useEffect(() => {
    onMount();
  }, [])

  // 식물 상태 업데이트 이후 메시지 띄우기
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
      <CContainer fluid>
        <CRow>
          {!hasPlantList
            ? <NoItem title="아직 정원에 아무도 없네요" button={<AddPlantButton size="lg" />} />
            : plantList.map((plant) =>
              <GardenCard
                plant={plant}
                fertilizerList={fertilizerList}
                setPlantList={setPlantList} 
                openNotification={openNotification}
                />
            )}
        </CRow>
      </CContainer>
    </>
  )
}

export default GardenMain
