import {LoadingOutlined} from "@ant-design/icons";
import React from "react";
import {Spin} from "antd";

const Loading = () => {
  const loading = <LoadingOutlined
    style={{
      fontSize: 70
    }}
    spin
  />;

  return (
    <div style={{height: "70vh"}} className={"d-flex justify-content-center align-items-center"}>
      <Spin className="text-success" indicator={loading}/>
    </div>
  )
}

export default Loading;
