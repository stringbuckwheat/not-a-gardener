import React, { useState, useEffect } from 'react'
import { CContainer, CRow } from '@coreui/react';
import NoItem from 'src/components/NoItem';
import AddPlantButton from 'src/components/button/AddPlantButton';
import GardenCard from 'src/components/card/GardenCard';
import { notification } from 'antd'
import getWateringNotificationMsg from 'src/utils/function/getWateringNotificationMsg';
import onMountGarden from 'src/api/service/onMountGarden';

const Garden = () => {
  const [hasPlantList, setHasPlantList] = useState(false);

  // 식물 리스트
  const [plantList, setPlantList] = useState([{
    plantNo: ''
    , plantName: ''
    , averageWateringPeriod: ''
    , wateringCode: ''
    , fertilizingCode: ''
  }]);

  // 약품 리스트
  const [chemicalList, setChemicalList] = useState([{
    chemicalNo: 0,
    chemicalName: ""
  }])

  // 백엔드에서 식물 리스트를 받아온다
  useEffect(() => {
    onMountGarden(setPlantList, setHasPlantList, setChemicalList);
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
                // chemicalList={chemicalList}
                setPlantList={setPlantList} 
                openNotification={openNotification}
                />
            )}
        </CRow>
      </CContainer>
    </>
  )
}

export default Garden
