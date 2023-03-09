import { CAlert } from "@coreui/react";
import AddPlantButton from "src/components/button/AddPlantButton";
import { useEffect, useState } from "react";
import authAxios from "src/utils/interceptors";
import PlantTable from "./PlantTable";
import PlantListLayout from "src/data-layout/PlantListLayout";
import PlantListTag from "./PlantListTag";

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

    useEffect(() => {
        authAxios.get("/plant")
            .then((res) => {
                console.log("res.data", res.data);
                setPlantList(res.data);
            })
    }, [plantList]);

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
