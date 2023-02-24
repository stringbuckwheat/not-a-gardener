import React from 'react'
import CIcon from '@coreui/icons-react'
import {
  cilBell,
  cilChartPie,
  cilDrop,
  cilPuzzle,
  cilStar,
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
    name: '정원 기록',
    to: '/garden/watering',
    icon: <CIcon icon={cilChartPie} customClassName="nav-icon" />,
  },
  {
    component: CNavTitle,
    name: '나의 계획',
  },
  {
    component: CNavItem,
    name: '나의 목표',
    to: '/garden/goal',
    icon: <CIcon icon={cilDrop} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: '위시리스트',
    to: '/garden/wishlist',
    icon: <CIcon icon={cilDrop} customClassName="nav-icon" />,
  },
  {
    component: CNavTitle,
    name: '나의 장비',
  },
  {
    component: CNavItem,
    name: '나의 장소',
    to: '/place',
    icon: <CIcon icon={cilDrop} customClassName="nav-icon" />,
  },
  {
    component: CNavGroup,
    name: '나의 비료',
    icon: <CIcon icon={cilPuzzle} customClassName="nav-icon" />,
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
    component: CNavGroup,
    name: '살충/살균제',
    to: '/garden/that',
    icon: <CIcon icon={cilPuzzle} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Charts',
    to: '/garden/chart',
    icon: <CIcon icon={cilChartPie} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: '무슨 기능',
    to: '/garden/what',
    icon: <CIcon icon={cilStar} customClassName="nav-icon" />,
    badge: {
      color: 'info',
      text: 'NEW',
    },
  },
]

export default SidebarList;
