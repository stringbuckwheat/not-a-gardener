import {DownOutlined, MenuFoldOutlined, MenuUnfoldOutlined,} from "@ant-design/icons";
import {Avatar, Dropdown, Space, Tag} from "antd";
import sprout from "../../assets/images/sprout.png";
import React from "react";
import logOut from "../../utils/function/logout";
import {useNavigate} from "react-router-dom";
import {Header} from "antd/es/layout/layout";
import {useDispatch, useSelector} from "react-redux";

const GardenHeader = () => {
  const navigate = useNavigate();
  const name = useSelector(state => state.sidebar.name);
  const collapsed = useSelector(state => state.sidebar.sidebarCollapsed);

  const dispatch = useDispatch();

  const items = [
    {
      key: 'edit',
      label: '회원 정보 수정',
      onClick: () => navigate("/me")
    },
    {
      key: 'logout',
      label: '로그아웃',
      onClick: logOut
    }
  ];

  const iconProps = {
    style: {fontSize: "1.2rem", color: "grey"},
    onClick: () => dispatch({type: 'setSidebar', payload: !collapsed})
  }

  return (
    <Header
      style={{
        display: 'flex',
        alignItems: 'center',
        padding: "1rem",
        justifyContent: "space-between"
      }}
    >
      {
        collapsed ? <MenuUnfoldOutlined {...iconProps}/>
          : <MenuFoldOutlined {...iconProps}/>
      }

      <Dropdown menu={{items}} trigger={['click']}>
        <Space style={{marginRight: "1rem"}}>
          <Avatar size="medium" src={sprout}/>
          <Tag color="#305738" size="large">{name}</Tag>
          <DownOutlined/>
        </Space>
      </Dropdown>
    </Header>
  )
}

export default GardenHeader;
