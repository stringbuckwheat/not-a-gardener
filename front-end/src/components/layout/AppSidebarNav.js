import React from 'react'
import {NavLink, useLocation} from 'react-router-dom'
import {CBadge} from '@coreui/react'

export const AppSidebarNav = ({items}) => {
  const location = useLocation()

  const navLink = (name, icon, badge) => {
    return (
      <>
        {icon && icon}
        {name && name}
        {badge && (
          <CBadge color={badge.color} className="ms-auto">
            {badge.text}
          </CBadge>
        )}
      </>
    )
  }

  // 사이드바 메뉴
  const navItem = (item, index) => {
    const {component, name, badge, icon, ...rest} = item
    const Component = component

    return (
      <Component
        {...(rest.to &&
          !rest.items &&
          {component: NavLink,}
        )}
        key={index}
        {...rest}
      >
        {navLink(name, icon, badge)}
      </Component>
    )
  }

  // 사이드바 그룹 함수
  const navGroup = (item, index) => {
    const {component, name, icon, to, ...rest} = item
    const Component = component

    return (
      <Component
        idx={String(index)}
        key={index}
        toggler={navLink(name, icon)}
        visible={location.pathname.startsWith(to)}
        {...rest}
      >
        {item.items?.map((item, index) =>
          item.items ? navGroup(item, index) : navItem(item, index),
        )}
      </Component>
    )
  }


  return (
    <>
      {items &&
        items.map((item, index) =>
          (item.items ? navGroup(item, index) : navItem(item, index)))
      }
    </>
  )
}
