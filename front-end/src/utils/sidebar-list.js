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
  cilHouse,
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
    name: '정원 기록',
    to: '/plant-log',
    icon: <CIcon icon={cilFlower} customClassName="nav-icon" />,
  },
  {
    component: CNavTitle,
    name: '나의 계획',
  },
  {
    component: CNavItem,
    name: '나의 목표',
    to: '/goal',
    icon: <CIcon icon={cilListNumbered} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: '나의 할일',
    to: '/todo',
    icon: <CIcon icon={cilCalendarCheck} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: '위시리스트',
    to: '/wishlist',
    icon: <CIcon icon={cilCart} customClassName="nav-icon" />,
  },
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
    component: CNavGroup,
    name: '나의 비료',
    icon: <CIcon icon={cilDrink} customClassName="nav-icon" />,
    items: [
      {
        component: CNavItem,
        name: '액체 비료',
        to: '/fertilizer',
      },
      {
        component: CNavItem,
        name: '완효성 비료',
        to: '/garden/inner2',
      }
    ],
  },
  {
    component: CNavItem,
    name: '살충/살균제',
    to: '/pesticide',
    icon: <CIcon icon={cilBug} customClassName="nav-icon" />,
  }
]

export default SidebarList;
