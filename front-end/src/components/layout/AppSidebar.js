import React from 'react'
import {useSelector, useDispatch} from 'react-redux'

import {CSidebar, CSidebarBrand, CSidebarNav} from '@coreui/react'

import {AppSidebarNav} from './AppSidebarNav'

import SimpleBar from 'simplebar-react'
import {ReactComponent as Logo} from "../../assets/images/logo.svg"

// 사이드바 내용들
import navigation from '../../utils/sidebar-list'
import {useNavigate} from "react-router-dom";

const AppSidebar = () => {
  const dispatch = useDispatch();
  const unfoldable = useSelector((state) => state.sidebar.sidebarUnfoldable);
  const sidebarShow = useSelector((state) => state.sidebar.sidebarShow);
  const navigate = useNavigate();

  return (
    <CSidebar
      className="bg-garden"
      position="fixed"
      unfoldable={unfoldable}
      visible={sidebarShow}
      onVisibleChange={(visible) => {
        dispatch({type: 'setSidebar', sidebarShow: visible})
      }}
    >
      <CSidebarBrand className="d-none d-md-flex">
        <Logo width="15vw" height="4vw" fill={"#E14A1E"} onClick={() => navigate("/")}/>
      </CSidebarBrand>
      <CSidebarNav>
        <SimpleBar>
          <AppSidebarNav items={navigation}/>
        </SimpleBar>
      </CSidebarNav>
    </CSidebar>
  )
}

export default React.memo(AppSidebar)
