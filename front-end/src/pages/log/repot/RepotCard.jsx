import {Table} from "antd";

const RepotCard = ({repot}) => {
  console.log("repot", repot);

  const columns = [
    {
      title: "분갈이 날짜",
      dataIndex: "repotDate",
      key: "repotDate",
    }
  ]

  return (
    <Table columns={columns} dataSource={data} />
    // <div style={{marginBottom: "1rem"}}>
    //   <div>{repot.repotDate}</div>
    //   <div>{repot.plantName}</div>
    //   <div>{repot.initPeriod == "Y" ? "물주기 초기화" : "물주기 유지"}</div>
    // </div>
  )
}

export default RepotCard;
