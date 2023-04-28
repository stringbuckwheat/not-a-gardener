import {LoadingOutlined} from "@ant-design/icons";
import React from "react";
import {Spin} from "antd";

/**
 * response나 컴포넌트를 기다릴 때 띄울 로딩 애니메이션
 * @returns {JSX.Element}
 * @constructor
 */
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
