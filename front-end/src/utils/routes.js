import React from 'react'

const GardenMain = React.lazy(() => import('../pages/garden/GardenMain'))
const Member = React.lazy(() => import('../pages/member/MemberDetail'))

const Place = React.lazy(() => import('../pages/place/Place'))
const AddPlace = React.lazy(() => import('../pages/place/AddPlace'))
const PlaceDetail = React.lazy(() => import('../pages/place/PlaceDetail'))

const Fertilizer = React.lazy(() => import('../pages/fertilizer/Fertilizer'))
const AddFertilizer =  React.lazy(() => import('../pages/fertilizer/AddFertilizer'));
const FertilizerDetail = React.lazy(() => import('../pages/fertilizer/FertilizerDetail'))

const Plant = React.lazy(() => import('../pages/plant/Plant'))
const AddPlant = React.lazy(() => import('../pages/plant/AddPlant'))
const PlantDetail = React.lazy(() => import('../pages/plant/PlantDetail'))

const Pesticide = React.lazy(() => import('../pages/pesticide/Pesticide'))
const AddPesticide = React.lazy(() => import('../pages/pesticide/AddPesticide'))
const PesticideDetail = React.lazy(() => import('../pages/pesticide/PesticideDetail'))

const routes = [
  { path: '/', exact: true, name: 'GardenMain', element: GardenMain },

  { path: '/member/:memberNo', name: 'member', element: Member},

  { path: '/place', name: 'place', element: Place},
  { path: '/place/add', name: 'add Place', element: AddPlace},
  { path: '/place/:placeNo', name: 'place detail', element: PlaceDetail},

  { path: '/fertilizer', name: 'fertilizer', element: Fertilizer},
  { path: '/fertilizer/:fertilizerNo', name: 'fertilizer detail', element: FertilizerDetail},
  { path: '/fertilizer/add', name: 'add fertilizer', element: AddFertilizer},

  { path: '/plant', exact: true, name: 'plant', element: Plant },
  { path: '/plant/add', exact: true, name: 'plant', element: AddPlant },
  { path: '/plant/:plantNo', exact: true, name: 'plant', element: PlantDetail },

  { path: '/pesticide', exact: true, name: 'pesticide', element: Pesticide },
  { path: '/pesticide/add', exact: true, name: 'add pesticide', element: AddPesticide },
  { path: '/pesticide/:pesticideNo', exact: true, name: 'pesticide detail', element: PesticideDetail },
]

export default routes
