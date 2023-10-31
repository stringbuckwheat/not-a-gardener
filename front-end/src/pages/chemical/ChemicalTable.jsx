import {Flex, Pagination, Table} from "antd";
import wateringTableColumnArray from "../../utils/dataArray/wateringTableColumnInChemicalArray";
import {useEffect, useState} from "react";
import getData from "../../api/backend-api/common/getData";

const ChemicalTable = ({chemicalId, wateringSize}) => {
  const [wateringList, setWateringList] = useState([]);
  const [page, setPage] = useState(1);
  const onChange = (page) => {
    setPage(() => page);
  }

  const paging = async () => {
    const res = await getData(`/chemical/${chemicalId}/watering?page=${page - 1}`);
    setWateringList(res);
  }

  useEffect(() => {
    paging();
  }, []);

  useEffect(() => {
    paging();
  }, [page])

  return (
    <>
      <Table
        pagination={false}
        columns={wateringTableColumnArray}
        dataSource={wateringList}/>
      <Flex justify={"right"} className={"mt-3"}>
        <Pagination defaultCurrent={1} total={wateringSize} onChange={onChange}/>
      </Flex>
    </>
  )
}

export default ChemicalTable
