import React, {Component, Suspense } from 'react';
import { HashRouter, Route, Routes } from 'react-router-dom'
import './scss/style.scss'


const loading = (
  <div className="pt-3 text-center">
    <div className="sk-spinner sk-spinner-pulse"></div>
  </div>
)

// Containers
const DefaultLayout = React.lazy(() => import('./layout/DefaultLayout'))

// Pages
const Login = React.lazy(() => import('./login/Login'))
const Register = React.lazy(() => import('./login/Register'))
// const Page404 = React.lazy(() => import('./views/pages/page404/Page404'))
const GardenMain = React.lazy(() => import('./garden/GardenMain'))


class App extends Component {
  render() {
    console.log("render()!");
    return (
      <HashRouter>
        <Suspense fallback={loading}>
          <Routes>

            <Route path="/" name="Login Page" element={<Login/>} />
            <Route path="/register" name="Register Page" element={<Register/>} />
            // <Route path="*" name="Home" element={<DefaultLayout />} />
            <Route path="/garden" name="GardenMain" element={<GardenMain />} />
          </Routes>
        </Suspense>
      </HashRouter>
    )
  }
}

export default App;
