import {ConfigProvider, Layout,} from 'antd';
import React, {Suspense} from "react";
import Sidebar from "./Sidebar";
import Loading from "../data/Loading";
import {Navigate, Route, Routes,} from "react-router-dom";
import routes from "../../utils/routes";
import {useDispatch} from "react-redux";
import GardenHeader from "./GardenHeader";

const {Footer, Content} = Layout;


const GardenLayout = () => {
  if (!localStorage.getItem("accessToken")) {
    return <Navigate to="/login" replace={true}/>
  }

  const dispatch = useDispatch();
  dispatch({type: 'setName', payload: localStorage.getItem("name")})

  return (
    <ConfigProvider
      theme={{
        components: {
          Layout: {
            headerBg: "white",
            siderBg: "white",
          },
        },
      }}
    >
      <Layout>
        <Sidebar/>
        <Layout>
          <GardenHeader/>
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

          <Footer style={{textAlign: "center"}}>
            <span style={{marginRight: "0.25rem", fontSize: "0.8rem"}}>not-a-gardener by memil</span>
          </Footer>
        </Layout>
      </Layout>
    </ConfigProvider>
  )
}

export default GardenLayout
