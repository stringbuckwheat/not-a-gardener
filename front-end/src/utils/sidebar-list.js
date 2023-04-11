import React from 'react'
import CIcon from '@coreui/icons-react'
import {
  cilBell,
  cilCart,
  cilListNumbered,
  cilDrink,
  cilCalendarCheck,
  cilBug,
  cilEco,
  cilFlower,
  cilHouse, cilDrop,
} from '@coreui/icons'
import { CNavGroup, CNavItem, CNavTitle } from '@coreui/react'

const SidebarList = [
  {
    component: CNavItem,
    name: '나의 정원',
    to: '/',
    icon: <CIcon icon={cilBell} customClassName="nav-icon" />,
  },
  {
    component: CNavTitle,
    name: '정원 기록',
  },
  {
    component: CNavItem,
    name: '나의 식물',
    to: '/plant',
    icon: <CIcon icon={cilEco} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: '물주기 기록',
    to: '/watering',
    icon: <CIcon icon={cilDrop} customClassName="nav-icon" />,
  },
  // {
  //   component: CNavItem,
  //   name: '정원 기록',
  //   to: '/plant-log',
  //   icon: <CIcon icon={cilFlower} customClassName="nav-icon" />,
  // },
  {
    component: CNavTitle,
    name: '나의 계획',
  },
  {
    component: CNavItem,
    name: '정원 계획',
    to: '/schedule',
    icon: <CIcon icon={cilCalendarCheck} customClassName="nav-icon" />,
  },
  // {
  //   component: CNavItem,
  //   name: '위시리스트',
  //   to: '/wishlist',
  //   icon: <CIcon icon={cilCart} customClassName="nav-icon" />,
  // },
  {
    component: CNavTitle,
    name: '나의 환경',
  },
  {
    component: CNavItem,
    name: '나의 장소',
    to: '/place',
    icon: <CIcon icon={cilHouse} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: '비료/살균/살충제',
    to: '/chemical',
    icon: <CIcon icon={cilDrink} customClassName="nav-icon" />,
  }
]

export default SidebarList;
