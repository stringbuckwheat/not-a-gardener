import React from 'react'

const GardenMain = React.lazy(() => import('./garden/GardenMain'))
const AddPlant = React.lazy(() => import('./garden/AddPlant'))

const routes = [
  { path: '/', exact: true, name: 'GardenMain', element: GardenMain },
  { path: '/addPlant', exact: true, name: 'addPlantForm', element: AddPlant },
]

export default routes
