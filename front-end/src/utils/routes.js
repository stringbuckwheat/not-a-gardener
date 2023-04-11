import React from 'react'

const Garden = React.lazy(() => import('../pages/garden/Garden'))
const Member = React.lazy(() => import('../pages/member/Member'))

const Place = React.lazy(() => import('../pages/place/Place'))
const PlaceDetail = React.lazy(() => import('../pages/place/PlaceDetail'))

const Chemical = React.lazy(() => import('../pages/chemical/Chemical'))
const ChemicalDetail = React.lazy(() => import('../pages/chemical/ChemicalDetail'))

const Plant = React.lazy(() => import('../pages/plant/Plant'))
const PlantDetail = React.lazy(() => import('../pages/plant/PlantDetail'))

const Schedule = React.lazy(() => import('../pages/schedule/Schedule'))

const Watering = React.lazy(() => import('../pages/watering/Watering'))

const routes = [
  {path: '/', exact: true, name: 'Garden', element: Garden},

  {path: '/member', name: 'member', element: Member},

  {path: '/place', name: 'place', element: Place},
  {path: '/place/:placeNo', name: 'place detail', element: PlaceDetail},

  {path: '/chemical', name: 'chemical', element: Chemical},
  {path: '/chemical/:chemicalNo', name: 'chemical detail', element: ChemicalDetail},

  {path: '/plant', exact: true, name: 'plant', element: Plant},
  {path: '/plant/:plantNo', exact: true, name: 'plant', element: PlantDetail},

  {path: '/schedule', exact: true, name: 'routine', element: Schedule},

  {path: '/watering', exact: true, name: 'watering', element: Watering},
]

export default routes
