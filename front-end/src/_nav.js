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

const _nav = [
  {
    component: CNavItem,
    name: 'Garden',
    to: '/garden',
    icon: <CIcon icon={cilBell} customClassName="nav-icon" />,
//    badge: {
//      color: 'info',
//      text: 'NEW',
//    },
  },
  {
    component: CNavTitle,
    name: '이후 구현 기능',
  },
  {
    component: CNavItem,
    name: '이런 기능',
    to: '/garden/this',
    icon: <CIcon icon={cilDrop} customClassName="nav-icon" />,
  },
  {
    component: CNavTitle,
    name: '이후 구현 기능',
  },
  {
    component: CNavGroup,
    name: '저런 기능',
    to: '/garden/that',
    icon: <CIcon icon={cilPuzzle} customClassName="nav-icon" />,
    items: [
      {
        component: CNavItem,
        name: '내부 기능',
        to: '/garden/inner',
      },
      {
        component: CNavItem,
        name: '내부 기능2',
        to: '/garden/inner2',
      }
    ],
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
//  {
//    component: CNavItem,
//    name: 'Docs',
//    href: 'https://coreui.io/react/docs/templates/installation/',
//    icon: <CIcon icon={cilDescription} customClassName="nav-icon" />,
//  },
]

export default _nav
