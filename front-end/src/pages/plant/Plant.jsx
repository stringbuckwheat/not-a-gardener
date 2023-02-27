import { CTable, CTableHead, CTableHeaderCell, CTableBody, CTableRow, CTableDataCell } from "@coreui/react";
import TableHead from "src/components/table/TableHead";
import TableBody from "src/components/table/TableBody";
import { useNavigate } from "react-router-dom";
import AddPlantButton from "src/components/button/AddPlantButton";

const Plant = () => {
    const navigate = useNavigate();

    const initObject = {
        plantNo: 1,
        plantName: '여우',
        placeName: '창가',
        medium: '흙과 화분',
        plantSpecies: '알로카시아 프라이덱',
        createDate: '2022-02-27'
    }

    const testPlantList = [{
        plantNo: 1,
        plantName: '여우',
        placeName: '창가',
        medium: '흙과 화분',
        plantSpecies: '알로카시아 프라이덱',
        createDate: '2022-02-27'
    },
    {
        plantNo: 1,
        plantName: '온시디움',
        placeName: '책상',
        medium: '수태',
        plantSpecies: '온시디움',
        createDate: '2022-02-27'
    }]

    const tableHeadArr = ["식물 이름", "장소", "재배법?", "종", "createDate"];
      
      const columns = [
        {
          title: '식물 이름',
          dataIndex: 'plantName',
          key: 'plantName',
          render: (plantNo) => {navigate('/plant/' + plantNo)}
        },
        {
          title: '장소',
          dataIndex: 'placeName',
          key: 'placeName',
        },
        {
          title: 'medium',
          dataIndex: 'medium',
          key: 'medium',
        },
        {
            title: '종',
            dataIndex: 'plantSpecies',
            key: 'plantSpecies',
        },
        {
            title: 'createDate',
            dataIndex: 'createDate',
            key: 'createDate',
        },
      ];

    return(
        <>
            <div className="mb-3">
                <AddPlantButton />
            </div>
            <CTable>
                <TableHead item={tableHeadArr}/>
                <TableBody list={testPlantList} />
            </CTable>
        </>
    )
}

export default Plant;
