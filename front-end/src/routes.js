import React from 'react'

const Garden = React.lazy(() => import('../../../../Desktop/React branch/src/pages/garden/Garden'))
const Gardener = React.lazy(() => import('../../../../Desktop/React branch/src/pages/gardener/Gardener'))

const Place = React.lazy(() => import('./pages/place/Place'))
const PlaceDetail = React.lazy(() => import('./pages/place/PlaceDetail'))

const Chemical = React.lazy(() => import('./pages/chemical/Chemical'))
const ChemicalDetail = React.lazy(() => import('./pages/chemical/ChemicalDetail'))

const Plant = React.lazy(() => import('../../../../Desktop/React branch/src/pages/plant/Plant'))
const PlantDetail = React.lazy(() => import('../../../../Desktop/React branch/src/pages/plant/plantDetail/PlantDetail'))

const Schedule = React.lazy(() => import('../../../../Desktop/React branch/src/pages/schedule/Schedule'))

const Watering = React.lazy(() => import('../../../../Desktop/React branch/src/pages/watering/Watering'))

const routes = [
  {path: '/', exact: true, name: 'Garden', element: Garden},

  {path: '/gardener', exact: true, name: 'gardener',element: Gardener},

  {path: '/place', name: 'place', element: Place},
  {path: '/place/:placeId', name: 'place detail', element: PlaceDetail},

  {path: '/chemical', name: 'chemical', element: Chemical},
  {path: '/chemical/:chemicalId', name: 'chemical detail', element: ChemicalDetail},

  {path: '/plant', exact: true, name: 'plant', element: Plant},
  {path: '/plant/:plantId', exact: true, name: 'plant', element: PlantDetail},

  {path: '/schedule', exact: true, name: 'routine', element: Schedule},

  {path: '/watering', exact: true, name: 'watering', element: Watering},
]

export default routes
