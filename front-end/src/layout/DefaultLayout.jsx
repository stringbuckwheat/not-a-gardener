import React from 'react'
import { Navigate } from 'react-router-dom';
import { AppContent, AppSidebar, AppFooter, AppHeader } from '../components/index'
import { isLogin } from './isLogin';

const DefaultLayout = () => {
  const accessToken = localStorage.getItem("login");
  console.log("accessToken: ", accessToken);

  return (
    accessToken
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
    : <Navigate to="/" replace={true}/>
    
  )
}

export default DefaultLayout
