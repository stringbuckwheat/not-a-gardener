import {Card, Table} from "antd";
import React from "react";
import GButton from "../../../components/button/GButton";
import AddRepot from "./AddRepot";

const RepotLog = ({repots, isRepotFormOpened, onClickRepotFormButton}) => {

  const mb = {marginBottom: "1rem"};

  const columns = [
    {
      title: "분갈이 날짜",
      dataIndex: "repotDate",
      key: "repotDate",
    },
    {
      title: "식물",
      dataIndex: "plantName",
      key: "plantName",
      render: (_, record) => <a href={`/plant/${record.plantId}`}>{record.plantName}</a>
    },
    {
      title: "물주기 초기화 여부",
      dataIndex: "initPeriod",
      key: "initPeriod",
      render: (init) => init == "Y" ? "물주기 초기화" : "물주기 유지"
    }
  ]

  return (
    <Card style={mb}>
      <div style={mb}>
        <span style={{fontSize: "1.25rem", fontWeight: "bold"}} className={"text-garden"}>나의 분갈이 기록</span>

        {
          !isRepotFormOpened
            ? <GButton color="garden" className="float-end" onClick={onClickRepotFormButton}>추가</GButton>
            : <></>
        }

      </div>

      {/* 분갈이 기록 추가 */}
      <div style={{marginBottom: "1.5rem"}}>
        {isRepotFormOpened
          ? <AddRepot
            onClickRepotFormButton={onClickRepotFormButton}
            addRepot={() => console.log("add repot method mocking")}/>
          : <></>}
      </div>

      {/* 분갈이 기록 리스트 */}
      {
        // repots.map((repot) => <RepotCard repot={repot} />)
        <Table columns={columns} dataSource={repots} />
      }
    </Card>
  )
}

export default RepotLog;
