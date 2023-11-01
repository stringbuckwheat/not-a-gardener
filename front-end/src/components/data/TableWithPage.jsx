import {Table} from "antd";
import {useEffect, useState} from "react";

const TableWithPage = ({columns, getDataSource, total, rowSelection, key}) => {
  const [dataSource, setDataSource] = useState([]);

  const paging = async (page) => {
    const res = await getDataSource(page);
    setDataSource(res);
  }

  useEffect(() => {
    paging();
  }, []);

  return (
    <>
      <Table
        rowSelection={rowSelection}
        pagination={{onChange: (page) => paging(page), total: total}}
        columns={columns}
        dataSource={dataSource}/>
    </>
  )
}

export default TableWithPage;
