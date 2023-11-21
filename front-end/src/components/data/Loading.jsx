import {LoadingOutlined} from "@ant-design/icons";
import React from "react";
import {Spin} from "antd";

/**
 * response나 컴포넌트를 기다릴 때 띄울 로딩 애니메이션
 * @returns {JSX.Element}
 * @constructor
 */
const Loading = () => {
  const loading = <LoadingOutlined style={{fontSize: "10rem"}} className="text-orange" spin/>;
  const style = {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    height: "100vh",
    backgroundColor: "white"
  };

  return (
    <div style={style}>
      <Spin indicator={loading}/>
    </div>
  )
}

export default Loading;
