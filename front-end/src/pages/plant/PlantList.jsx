import PlantTable from "../../components/table/PlantTable";
import PlantListLayout from "src/components/data/layout/PlantListLayout";
import PlantListTag from "../../components/tag/PlantListTag";
import { useEffect, useState } from "react";
import onMount from "src/api/service/onMount";

const PlantList = (props) => {
    const plantList = props.plantList;
    const setPlantList = props.setPlantList;

    const [placeList, setPlaceList] = useState([])

    useEffect(() => {
        onMount("/place", setPlaceList);
    }, []);

    return (
        <PlantListLayout
            title="나의 식물"
            url="/plant"
            deleteTitle="식물"
            tags={<PlantListTag howManyPlants={plantList.length} />}
            bottomData={<PlantTable 
                            plantList={plantList} 
                            setPlantList={setPlantList}
                            placeList={placeList}/>}
        />
    )
}

export default PlantList;
