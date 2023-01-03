import React from 'react'

const GardenMain = React.lazy(() => import('./garden/GardenMain'))

const routes = [
  { path: '/garden', exact: true, name: 'GardenMain', element: GardenMain },
]

export default routes
