import React, {Suspense} from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom'
import Loading from "./components/data/Loading";
import {ConfigProvider} from "antd";
import themeOrange from "./theme/themeOrange";

// Pages
const Login = React.lazy(() => import('./pages/login/Login'))
const ForgotAccount = React.lazy(() => import('./pages/login/ForgotAccount'))
const Register = React.lazy(() => import('./pages/login/Register'))
const GardenLayout = React.lazy(() => import('./components/layout/GardenLayout'))
const GetToken = React.lazy(() => import('./pages/login/GetToken'))

const App = () => {
  // console.log = function no_console() {};
  // console.debug = function no_console() {};
  // console.info = function no_console() {};
  // console.warn = function no_console() {};
  // console.error = function no_console() {};

  return (
    <ConfigProvider theme={themeOrange}>
      <BrowserRouter>
        <Suspense fallback={<Loading/>}>
          <Routes>
            <Route path="/login" name="Login Page" element={<Login/>}/>
            <Route path="/oauth/:accessToken" element={<GetToken/>}/>
            <Route path="/forgot" name="Forgot Account" element={<ForgotAccount/>}/>
            <Route path="/register" name="Register Page" element={<Register/>}/>
            <Route path="*" element={<GardenLayout/>}/>
          </Routes>
        </Suspense>
      </BrowserRouter>
    </ConfigProvider>
  )
}

export default App;
