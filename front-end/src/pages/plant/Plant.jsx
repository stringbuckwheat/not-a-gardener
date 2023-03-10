import { useEffect, useState } from "react";
import authAxios from "src/utils/interceptors";
import PlantTable from "./PlantTable";
import PlantListLayout from "src/data-layout/PlantListLayout";
import PlantListTag from "./PlantListTag";
import { useLocation } from "react-router-dom";

const Plant = () => {
    const [plantList, setPlantList] = useState([{
        plantNo: 0,
        plantName: '',
        placeNo: 0,
        placeName: '',
        medium: '',
        plantSpecies: '',
        createDate: ''
    }]);

    const { state } = useLocation();
    console.log("plant 메인 페이지 state", state);

    useEffect(() => {
        authAxios.get("/plant")
            .then((res) => {
                console.log("res.data", res.data);
                setPlantList(res.data);
            })
    }, []);

    useEffect(() => {
        if(state != null){
            authAxios.get("/plant")
            .then((res) => {
                console.log("res.data", res.data);
                setPlantList(res.data);
            })
        }
    }, [state])

    return (
        <PlantListLayout
            title=""
            url="/plant"
            path={"test"}
            deleteTitle="식물"
            tags={<PlantListTag howManyPlants={plantList.length} />}
            bottomData={<PlantTable plantList={plantList} />}
        />
    )
}

export default Plant;
