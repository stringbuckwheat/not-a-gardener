import React from 'react'

const Garden = React.lazy(() => import('../pages/garden/Garden'))
const Member = React.lazy(() => import('../pages/member/MemberDetail'))

const Place = React.lazy(() => import('../pages/place/Place'))
const AddPlace = React.lazy(() => import('../pages/place/AddPlace'))
const PlaceDetail = React.lazy(() => import('../pages/place/PlaceDetail'))

const Chemical =  React.lazy(() => import('../pages/chemical/Chemical'))
const AddChemical =  React.lazy(() => import('../pages/chemical/AddChemical'))
const ChemicalDetail =  React.lazy(() => import('../pages/chemical/ChemicalDetail'))

const Plant = React.lazy(() => import('../pages/plant/Plant'))
const AddPlant = React.lazy(() => import('../pages/plant/AddPlant'))
const PlantDetail = React.lazy(() => import('../pages/plant/PlantDetail'))

const routes = [
  { path: '/', exact: true, name: 'Garden', element: Garden },

  { path: '/member/:memberNo', name: 'member', element: Member},

  { path: '/place', name: 'place', element: Place},
  { path: '/place/add', name: 'add Place', element: AddPlace},
  { path: '/place/:placeNo', name: 'place detail', element: PlaceDetail},

  { path: '/chemical', name: 'chemical', element: Chemical},
  { path: '/chemical/add', name: 'add chemical', element: AddChemical},
  { path: '/chemical/:chemicalNo', name: 'chemical detail', element: ChemicalDetail},

  { path: '/plant', exact: true, name: 'plant', element: Plant },
  { path: '/plant/add', exact: true, name: 'plant', element: AddPlant },
  { path: '/plant/:plantNo', exact: true, name: 'plant', element: PlantDetail },

]

export default routes
