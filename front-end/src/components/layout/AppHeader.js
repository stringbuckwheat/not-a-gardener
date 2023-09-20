import React from 'react'
import { useSelector, useDispatch } from 'react-redux'
import {
  CContainer,
  CHeader,
  CHeaderNav,
  CHeaderToggler
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import { cilMenu } from '@coreui/icons'
import { AppHeaderDropdown } from '../header'

const AppHeader = () => {
  const dispatch = useDispatch()
  const sidebarShow = useSelector((state) => state.sidebar.sidebarShow);
  console.log("sidebarShow", sidebarShow);

  return (
    <CHeader position="sticky" className="mb-3 bg-white">
      <CContainer fluid>
      <CHeaderToggler
          className="ps-1"
          onClick={() => dispatch({ type: 'setSidebar', sidebarShow: !sidebarShow })}
        >
          <CIcon icon={cilMenu} size="lg"/>
        </CHeaderToggler>
        <CHeaderNav>
          <AppHeaderDropdown />
        </CHeaderNav>
      </CContainer>
    </CHeader>
  )
}

export default AppHeader
