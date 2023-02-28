import { CButton } from "@coreui/react"
import { useState, useEffect } from "react"
import { Link } from "react-router-dom"
import authAxios from "src/utils/requestInterceptor"

const AddPlantButton = (props) => {
    const [ placeList, setPlaceList ] = useState([{}]);
    const [ initPlant, setInitPlant ] = useState({});

    useEffect(() => {
        authAxios.get("/place")
        .then((res) => {
          const arr = [];
  
          res.data.map((item) => {
            arr.push({
              key: item.placeNo,
              value: item.placeName
            })
          })
  
          setPlaceList(arr);
          setInitPlant({
            plantName: "",
            plantSpecies: "",
            placeNo: arr[0].key,
            medium: "흙과 화분",
            earlyWateringPeriod: 5
          })
        })
    }, [])

    return (
        <Link to="/plant/add" state={{placeList: placeList, initPlant: initPlant}}>
            <CButton color="success" size={props.size} variant="outline" shape="rounded-pill">식물 추가하기</CButton>
        </Link>
    )
}

export default AddPlantButton