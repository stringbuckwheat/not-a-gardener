import { CTable, CAlert } from "@coreui/react";
import TableHead from "src/components/table/TableHead";
import PlantTableBody from "src/components/table/PlantTableBody";
import AddPlantButton from "src/components/button/AddPlantButton";
import { useEffect, useState } from "react";
import authAxios from "src/utils/requestInterceptor";

const Plant = () => {
    const [ plantList, setPlantList ] = useState([{
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
    }, []);

    const tableHeadArr = ["식물 이름", "종", "장소", "식재 환경", "평균 물주기", "createDate"];
    const keySet = ["plantName", "plantSpecies", "placeName", "medium", "averageWateringPeriod", "createDate"]

    return(
        <>
            <div className="mb-3">
                <CAlert color="dark">{plantList.length}개의 식물이 함께하고 있어요!</CAlert>
                <AddPlantButton size="sm"/>
            </div>
            <CTable hover>
                <TableHead item={tableHeadArr}/>
                <PlantTableBody list={plantList} keySet={keySet} linkUrl="/plant/" />
            </CTable>
        </>
    )
}

export default Plant;
