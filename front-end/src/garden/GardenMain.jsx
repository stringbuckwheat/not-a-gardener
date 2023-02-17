import React, { useState, useEffect }  from 'react'

import authAxios from '../requestInterceptor'
import NoPlant from './NoPlant';
import Garden from './Garden';

const GardenMain = () => {
  // 식물 리스트
  const [plantList, setPlantList] = useState([{
      plantNo: ''
      , plantName: ''
      , averageWateringPeriod: ''
      , wateringCode: ''
      , fertilizingCode: ''
  }]);

  const hasPlant = useState(false);

  // 백엔드에서 식물 리스트를 받아온다
   useEffect(() => {
     authAxios.get("/garden", "")
         .then((res) => {
           setPlantList(res.data);
           console.log(res.data);

           hasPlant(res.data)
           })
         .catch(error => console.log(error));
   }, [])

  return (
  <>{plantList.length == 0 
    ? <NoPlant />
    : <Garden plantList={plantList}/>}
  </>  
  )
}

export default GardenMain
