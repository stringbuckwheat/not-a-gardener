import React from 'react'

const GardenMain = React.lazy(() => import('./garden/GardenMain'))
const AddPlant = React.lazy(() => import('./garden/AddPlant'))
const ModifyPlant = React.lazy(() => import('./plant/Plant'))

const routes = [
  { path: '/', exact: true, name: 'GardenMain', element: GardenMain },
  { path: '/addPlant', exact: true, name: 'addPlantForm', element: AddPlant },
  { path: '/modify-plant/:plantNo', exact: true, name: 'modifyPlant', element: ModifyPlant },
]

export default routes
