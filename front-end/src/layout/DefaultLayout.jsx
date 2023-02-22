import React from 'react'
import { Navigate } from 'react-router-dom';
import { AppContent, AppSidebar, AppFooter, AppHeader } from '../components/index'

const DefaultLayout = () => {
  console.log("default layout")
  const isLogin = localStorage.getItem("login");
  console.log("login", isLogin !== undefined)

  return (
    isLogin
    ? 
    <div>
      <AppSidebar />
      <div className="wrapper d-flex flex-column min-vh-100 bg-light">
        <AppHeader />
        <div className="body flex-grow-1 px-3">
          <AppContent />
        </div>
        <AppFooter />
      </div>
    </div>
    : <Navigate to="/login" replace={true}/>
  )
}

export default DefaultLayout
