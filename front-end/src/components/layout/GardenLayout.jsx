import {Button, ConfigProvider, Dropdown, Layout, Menu, Space, Tag} from 'antd';
import React, {Suspense, useState} from "react";
import {ReactComponent as Logo} from "../../assets/images/logo.svg";

const {Footer, Content} = Layout;

import {
  DownOutlined,
  MenuFoldOutlined,
} from '@ant-design/icons';
import Sidebar from "./Sidebar";
import Loading from "../data/Loading";
import {Navigate, Route, Routes, useNavigate} from "react-router-dom";
import routes from "../../utils/routes";
import {useDispatch} from "react-redux";
import {CAvatar} from "@coreui/react";
import sprout from "../../assets/images/sprout.png";
import logOut from "../../utils/function/logout";
import GardenHeader from "./GardenHeader";

const GardenLayout = () => {
  if (!localStorage.getItem("accessToken")) {
    return <Navigate to="/login" replace={true}/>
  }

  const dispatch = useDispatch();
  dispatch({type: 'setName', name: localStorage.getItem("name")})

  const [collapsed, setCollapsed] = useState(false);

  return (
    <ConfigProvider
      theme={{
        components: {
          Layout: {
            headerBg: "white",
            siderBg: "white"
          },
        },
      }}
    >
      <Layout>
        <Sidebar collapsed={collapsed} setCollapsed={setCollapsed}/>

        <Layout>
          <GardenHeader collapsed={collapsed} setCollapsed={setCollapsed}/>
          <Content
            style={{
              padding: '1rem 2rem',
            }}
          >
            <Suspense fallback={<Loading/>}>
              <Routes>
                {routes.map((route, idx) => {
                  return (
                    route.element && (
                      <Route
                        key={idx}
                        path={route.path}
                        exact={route.exact}
                        name={route.name}
                        element={<route.element/>}
                      />
                    )
                  )
                })}
              </Routes>
            </Suspense>
          </Content>

          <Footer className={"text-center"}>
            <span className="ms-1 small">not-a-gardener by memil</span>
          </Footer>
        </Layout>


      </Layout>
    </ConfigProvider>

  )
}

export default GardenLayout
