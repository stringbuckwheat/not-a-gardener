import React from 'react'

const GardenMain = React.lazy(() => import('../pages/garden/GardenMain'))
const Member = React.lazy(() => import('../pages/member/MemberDetail'))

const AddPlant = React.lazy(() => import('../pages/garden/AddPlant'))
const ModifyPlant = React.lazy(() => import('../pages/plant/Plant'))

const Place = React.lazy(() => import('../pages/place/Place'))
const PlaceDetail = React.lazy(() => import('../pages/place/PlaceDetail'))

const Fertilizer = React.lazy(() => import('../pages/fertilizer/Fertilizer'))
const FertilizerDetail = React.lazy(() => import('../pages/fertilizer/FertilizerDetail'))

const routes = [
  { path: '/', exact: true, name: 'GardenMain', element: GardenMain },
  { path: '/addPlant', exact: true, name: 'addPlantForm', element: AddPlant },
  { path: '/modify-plant/:plantNo', exact: true, name: 'modifyPlant', element: ModifyPlant },
  { path: '/member/:memberNo', name: 'member', element: Member},
  { path: '/place', name: 'place', element: Place},
  { path: '/place/:placeNo', name: 'place detail', element: PlaceDetail},
  { path: '/place/add', name: 'add Place', element: PlaceDetail},
  { path: '/fertilizer', name: 'fertilizer', element: Fertilizer},
  { path: '/fertilizer/:fertilizerNo', name: 'fertilizer detail', element: FertilizerDetail},
  { path: '/fertilizer/add', name: 'add fertilizer', element: FertilizerDetail},
]

export default routes
