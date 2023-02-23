import React, { Suspense } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import './scss/style.scss'


const loading = (
  <div className="pt-3 text-center">
    <div className="sk-spinner sk-spinner-pulse"></div>
  </div>
)

// Pages
const Login = React.lazy(() => import('./pages/login/Login'))
const Register = React.lazy(() => import('./pages/login/Register'))
const DefaultLayout = React.lazy(() => import('./components/layout/DefaultLayout'))
const GetToken = React.lazy(() => import('./pages/login/GetToken'))

function App() {
    console.log("render()!");

    return (
        <BrowserRouter>
          <Suspense fallback={loading}>
            <Routes>
              <Route path="/login" name="Login Page" element={<Login />}/>
              <Route path="/oauth/:token" element={<GetToken />} />
              <Route path="/register" name="Register Page" element={<Register />} />
              <Route path="*" element={<DefaultLayout />} />
            </Routes>
          </Suspense>
        </BrowserRouter>
    )
  }


export default App;
