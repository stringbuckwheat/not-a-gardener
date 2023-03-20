import { useEffect, useState } from "react";
import onMountWithLength from "src/api/service/onMountWithLength";
import AddPlantButton from "src/components/button/AddPlantButton";
import NoItem from "src/components/NoItem";
import PlantList from "./PlantList";

const Plant = () => {
    const [hasPlant, setHasPlant] = useState(false);
    const [plantList, setPlantList] = useState([]);

    useEffect(() => {
        onMountWithLength("/garden", setPlantList, setHasPlant);
        console.log("plantList", plantList);
    }, [])

    return (
        <>
            {
                !hasPlant
                    ? <NoItem title="등록된 식물이 없어요" button={<AddPlantButton addUrl="/plant/add" size="lg" title="식물 추가하기" />} />
                    : <PlantList plantList={plantList} setPlantList={setPlantList} />
            }
        </>
    )
}

export default Plant;