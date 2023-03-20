import { Table } from "antd";

const ChemicalUsage = (props) => {
    const wateringTableColumnArray = props.wateringTableColumnArray;
    const wateringList = props.wateringList;

    // const [onAdd, setOnAdd] = useState(false);

    return (
        <>
            {/* {
                !onAdd
                    ?
                    <CButton
                        className="float-end mb-2"
                        onClick={() => { setOnAdd(true) }}
                        color="success"
                        variant="outline"
                        size="sm">사용 내역 추가</CButton>
                    :
                    <AddChemicalUsage setOnAdd={setOnAdd}/>
            } */}

            <Table
                columns={wateringTableColumnArray}
                dataSource={wateringList} />
        </>
    )
}

export default ChemicalUsage;