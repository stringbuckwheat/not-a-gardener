import React, { useState, useEffect } from 'react'
import { CContainer, CRow } from '@coreui/react';
import authAxios from '../../utils/interceptors'
import NoItem from 'src/components/NoItem';
import AddPlantButton from 'src/components/button/AddPlantButton';
import GardenCard from 'src/components/card/GardenCard';

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

  // 백엔드에서 식물 리스트를 받아온다
  useEffect(() => {
    authAxios.get("/garden")
      .then((res) => {
        console.log("res", res.data);

        res.data.sort((a, b) => {
          return a.wateringCode - b.wateringCode;
        })

        if (res.data.length !== 0) {
          setHasPlantList(true);
          setPlantList(res.data);
        }

      })
  }, [])

  return (
    <CContainer fluid>
      <CRow>
        {!hasPlantList
          ? <NoItem title="아직 정원에 아무도 없네요" button={<AddPlantButton size="lg" />} />
          : plantList.map((plant) =>
            <GardenCard plant={plant} />
          )}
      </CRow>
    </CContainer>
  )
}

export default GardenMain
