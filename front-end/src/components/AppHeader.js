import React from 'react'
import { NavLink } from 'react-router-dom'
import { useSelector, useDispatch } from 'react-redux'
import {
  CContainer,
  CHeader,
  CHeaderBrand,
  CHeaderDivider,
  CHeaderNav,
  CHeaderToggler,
  CNavLink,
  CNavItem,
  CButton,
  CNav
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import { cilBell, cilEnvelopeOpen, cilList, cilMenu } from '@coreui/icons'
import { AppHeaderDropdown } from './header/index'

const AppHeader = () => {
  const dispatch = useDispatch()
  const sidebarShow = useSelector((state) => state.sidebarShow)

  const name = "test";

  return (
    <CHeader position="sticky" className="mb-4">
      <CContainer fluid>
      <CHeaderToggler
          className="ps-1"
          onClick={() => dispatch({ type: 'set', sidebarShow: !sidebarShow })}
        >
          <CIcon icon={cilMenu} size="lg"/>
        </CHeaderToggler>
        <CHeaderBrand className="mx-auto d-md-none" to="/garden">
           Not a Gardener 
        </CHeaderBrand>
        <CHeaderNav className="ms-3">
          <div className="ms-3 mt-2">안녕하세요, {name}님</div>
          <AppHeaderDropdown />
        </CHeaderNav>
      </CContainer>
    </CHeader>
  )
}

export default AppHeader
