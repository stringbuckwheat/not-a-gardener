import getGarden from "../backend-api/garden/getGarden";
import onMount from "./onMount";

const onMountGarden = async (setPlantList, setHasPlantList, setChemicalList) => {
    const gardenList = await getGarden();

    if (gardenList.length !== 0) {
        setHasPlantList(true);
        setPlantList(gardenList);
    }

    onMount("/chemical", setChemicalList);
}

export default onMountGarden;