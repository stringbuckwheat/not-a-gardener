import React, { useState, useEffect }  from 'react'

import authAxios from '../../utils/requestInterceptor'
import NoPlant from './NoPlant';
import Garden from './Garden';

const GardenMain = () => {
  console.log("garden main")
  // 식물 리스트
  const [plantList, setPlantList] = useState([{
      plantNo: ''
      , plantName: ''
      , averageWateringPeriod: ''
      , wateringCode: ''
      , fertilizingCode: ''
  }]);

  const [ hasPlant, setHasPlant ] = useState(false);

  // 백엔드에서 식물 리스트를 받아온다
   useEffect(() => {
    console.log("Garden Main -- /garden")
    authAxios.get("/garden")
        .then((res) => {
          setHasPlant(res.data.length != 0);
          // console.log("res.data", res.data);

          // if(hasPlant){
          //   setPlantList(res.data);
          // }
          
        })
        .catch((error) => {
          console.log("authAxios.get('garden') error")
          console.log(error);
        });
   }, [])

  return (
  <>{!hasPlant 
    ? <NoPlant />
    : <Garden plantList={plantList}/>}
  </>  
  )
}

export default GardenMain
