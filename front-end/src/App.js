import React, {Component, Suspense } from 'react';
import { BrowserRouter, HashRouter, Route, Routes } from 'react-router-dom'
import './scss/style.scss'


const loading = (
  <div className="pt-3 text-center">
    <div className="sk-spinner sk-spinner-pulse"></div>
  </div>
)

// Pages
const Login = React.lazy(() => import('./login/Login'))
const Register = React.lazy(() => import('./login/Register'))
const DefaultLayout = React.lazy(() => import('./layout/DefaultLayout'))
const GetToken = React.lazy(() => import('./login/GetToken'))

class App extends Component {
  render() {
    console.log("render()!");
    return (
      <BrowserRouter>
        <Suspense fallback={loading}>
          <Routes>
            <Route path="/register" name="Register Page" element={<Register />} />
            <Route path="/garden/*" element = {<DefaultLayout />} />
            <Route path="/oauth/:token" element={<GetToken />} />
            <Route path="/" name="Login Page" element={<Login />}/>
          </Routes>
        </Suspense>
      </BrowserRouter>
    )
  }
}

export default App;
