import React from 'react'

const GardenMain = React.lazy(() => import('../pages/garden/GardenMain'))
const AddPlant = React.lazy(() => import('../pages/garden/AddPlant'))
const ModifyPlant = React.lazy(() => import('../pages/plant/Plant'))
const Member = React.lazy(() => import('../pages/member/MemberDetail'))
const Place = React.lazy(() => import('../pages/place/Place'))
const PlaceOne = React.lazy(() => import('../pages/place/PlaceOne'))
const AddPlace = React.lazy(() => import('../pages/place/AddPlace'))

const routes = [
  { path: '/', exact: true, name: 'GardenMain', element: GardenMain },
  { path: '/addPlant', exact: true, name: 'addPlantForm', element: AddPlant },
  { path: '/modify-plant/:plantNo', exact: true, name: 'modifyPlant', element: ModifyPlant },
  { path: '/member/:memberNo', name: 'member', element: Member},
  { path: '/place', name: 'place', element: Place},
  { path: '/place/:placeNo', name: 'place detail', element: PlaceOne},
  { path: '/place/add', name: 'add Place', element: AddPlace}
]

export default routes
