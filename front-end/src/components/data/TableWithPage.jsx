import {Flex, Pagination, Table} from "antd";
import {useEffect, useState} from "react";

const TableWithPage = ({columns, getDataSource, total, rowSelection, key}) => {
  const [dataSource, setDataSource] = useState([]);
  const [page, setPage] = useState(1);

  const paging = async () => {
    console.log("paging async method");
    const res = await getDataSource(page);
    console.log("paging res", res);
    setDataSource(res);
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
        rowSelection={rowSelection}
        pagination={false}
        columns={columns}
        dataSource={dataSource}/>
      <Flex justify={"right"} className={"mt-3"}>
        <Pagination defaultCurrent={1} total={total} onChange={(page) => setPage(page)}/>
      </Flex>
    </>
  )
}

export default TableWithPage;
