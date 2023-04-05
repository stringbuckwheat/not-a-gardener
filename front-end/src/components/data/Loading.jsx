import {LoadingOutlined} from "@ant-design/icons";
import React from "react";
import {Spin} from "antd";

const Loading = () => {
  const loading = <LoadingOutlined style={{fontSize: "10vw"}} className="text-orange" spin/>;

  return (
    <div
      className="d-flex justify-content-center align-items-center height-70vh">
      <Spin indicator={loading}/>
    </div>
  )
}

export default Loading;
