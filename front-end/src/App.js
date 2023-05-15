import React, {Suspense} from 'react';
import {BrowserRouter, Route, Routes} from 'react-router-dom'
import './scss/style.scss'
import Loading from "./components/data/Loading";

// Pages
const Landing = React.lazy(() => import('./pages/landing/Landing'))
const Login = React.lazy(() => import('./pages/login/Login'))
const ForgotAccount = React.lazy(() => import('./pages/login/ForgotAccount'))
const Register = React.lazy(() => import('./pages/login/Register'))
const GardenLayout = React.lazy(() => import('./components/layout/GardenLayout'))
const GetToken = React.lazy(() => import('./pages/login/GetToken'))

const App = () => {
  return (
    <BrowserRouter>
      <Suspense fallback={<Loading/>}>
        <Routes>
          {/*<Route path="" name="Landing" element={<Landing/>}/>*/}
          <Route path="/login" name="Login Page" element={<Login/>}/>
          <Route path="/oauth/:token" element={<GetToken/>}/>
          <Route path="/forgot" name="Forgot Account" element={<ForgotAccount/>}/>
          <Route path="/register" name="Register Page" element={<Register/>}/>
          <Route path="*" element={<GardenLayout/>}/>
        </Routes>
      </Suspense>
    </BrowserRouter>
  )
}

export default App;
