import {Menu} from "antd";
import Sider from "antd/es/layout/Sider";
import {ReactComponent as Logo} from "../../assets/images/logo.svg";
import React, {useState} from "react";
import {Link, Navigate, useNavigate} from "react-router-dom";

const items = [
  {
    label: <><span style={{fontSize: "1.2rem", marginRight: "0.7rem"}}>🏡</span> 나의 정원</>,
    key: "/",
  },
  // 정원 기록
  {
    label: <><span style={{fontSize: "1.2rem", marginRight: "0.7rem"}}>🌿</span> 나의 식물</>,
    key: "/plant",
  },
  {
    label: <><span style={{fontSize: "1.2rem", marginRight: "0.7rem"}}>💧</span> 물주기 기록</>,
    key: "/watering"
  },
  // 나의 계획
  {
    label: <><span style={{fontSize: "1.2rem", marginRight: "0.7rem"}}>✏️ </span> 정원 계획</>,
    key: "/schedule"
  },
  // 나의 환경
  {
    label: <><span style={{fontSize: "1.2rem", marginRight: "0.7rem"}}>🛋️ </span> 나의 장소</>,
    key: "/place"
  },
  {
    label: <><span style={{fontSize: "1.2rem", marginRight: "0.7rem"}}>🐜 </span> 비료/살균/살충제</>,
    key: "/chemical"
  }
]


const Sidebar = ({collapsed, setCollapsed}) => {
  const [current, setCurrent] = useState("/");
  const navigate = useNavigate();

  const onMenu = (e) => {
    navigate(e.key);
    setCurrent(e.key);
  }

  return (
    <Sider
      breakpoint="lg"
      collapsedWidth="0"
      onCollapse={(collapsed, type) => setCollapsed(collapsed)}
      collapsed={collapsed}
      style={{minHeight: "100vh", minWidth: "30vw"}}
    >
      <div style={{justifyContent: "center", display: "flex"}}>
        {/* todo header 높이에 맞추기*/}
        <Logo width="13vw" height="4vw" fill={"green"} onClick={() => navigate("/")} style={{padding: "0.2rem",}}/>
      </div>
      <Menu
        defaultSelectedKeys={['/']}
        onClick={onMenu}
        selectedKeys={[current]}
        mode="inline"
        items={items}
      />
    </Sider>
  )
}

export default Sidebar
