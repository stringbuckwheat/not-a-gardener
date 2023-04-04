import React from 'react'
import {Navigate} from 'react-router-dom';
import {AppContent, AppSidebar, AppFooter, AppHeader} from '../index'

const GardenLayout = () => {
  const isLogin = localStorage.getItem("login");
  console.log("= Default Layout: login", isLogin !== undefined);

  if (!isLogin) {
    return <Navigate to="/login" replace={true}/>
  }

  return (
    <div>
      <AppSidebar/>
      <div className="wrapper d-flex flex-column min-vh-100 bg-light">
        <AppHeader/>
        <div className="body flex-grow-1 px-3">
          <AppContent/>
        </div>
        <AppFooter/>
      </div>
    </div>
  )
}

export default GardenLayout
