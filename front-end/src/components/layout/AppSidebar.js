import React from 'react'
import {useSelector, useDispatch} from 'react-redux'

import {CSidebar, CSidebarBrand, CSidebarNav} from '@coreui/react'

import {AppSidebarNav} from './AppSidebarNav'

import SimpleBar from 'simplebar-react'
// import 'simplebar/dist/simplebar.min.css'
import {ReactComponent as Logo} from "../../assets/images/logo.svg"

// 사이드바 내용들
import navigation from '../../utils/sidebar-list'
import {useNavigate} from "react-router-dom";

const AppSidebar = () => {
  const dispatch = useDispatch()
  const unfoldable = useSelector((state) => state.sidebarUnfoldable)
  const sidebarShow = useSelector((state) => state.sidebarShow)
  const navigate = useNavigate();

  return (
    <CSidebar
      className="bg-garden"
      position="fixed"
      unfoldable={unfoldable}
      visible={sidebarShow}
      onVisibleChange={(visible) => {
        dispatch({type: 'set', sidebarShow: visible})
      }}
    >
      <CSidebarBrand className="d-none d-md-flex">
        <Logo width={"60%"} height={"auto"} fill={"#E14A1E"} onClick={() => navigate("/")}/>
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
