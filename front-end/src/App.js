import React, {Component, Suspense } from 'react';
import { HashRouter, Route, Routes } from 'react-router-dom'
import './scss/style.scss'


const loading = (
  <div className="pt-3 text-center">
    <div className="sk-spinner sk-spinner-pulse"></div>
  </div>
)

// Containers
// const DefaultLayout = React.lazy(() => import('./layout/DefaultLayout'))

// Pages
const Login = React.lazy(() => import('./login/Login'))
const Register = React.lazy(() => import('./login/Register'))

class App extends Component {
  render() {
    return (
      <HashRouter>
        <Suspense fallback={loading}>
          <Routes>

            <Route path="/" name="Login Page" element={<Login/>} />
            <Route path="/register" name="Register Page" element={<Register/>} />

          </Routes>
        </Suspense>
      </HashRouter>
    )
  }
}

export default App;
