import React from 'react'
import {Navigate} from 'react-router-dom';
import AppSidebar from "./AppSidebar";
import AppHeader from "./AppHeader";
import AppContent from "./AppContent";
import AppFooter from "./AppFooter";

/**
 * 로그인 이후 모든 페이지의 레이아웃
 * 페이지에 따라 AppContent 부분만 바꿔끼움
 * @returns {JSX.Element}
 * @constructor
 */
const GardenLayout = () => {
  if (!localStorage.getItem("accessToken")) {
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
