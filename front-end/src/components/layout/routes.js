import React from 'react'

const Garden = React.lazy(() => import('../../pages/garden/Garden'))
const Gardener = React.lazy(() => import('../../pages/gardener/Gardener'))

const Place = React.lazy(() => import('../../pages/place/Place'))
const PlaceDetail = React.lazy(() => import('../../pages/place/PlaceDetail'))

const Chemical = React.lazy(() => import('../../pages/chemical/Chemical'))
const ChemicalDetail = React.lazy(() => import('../../pages/chemical/ChemicalDetail'))

const Plant = React.lazy(() => import('../../pages/plant/Plant'))
const PlantDetail = React.lazy(() => import('../../pages/plant_detail/PlantDetail'))

const Schedule = React.lazy(() => import('../..//pages/schedule/Schedule'))

const Watering = React.lazy(() => import('../../pages/watering/Watering'))

const Log = React.lazy(() => import('../../pages/log/Log'))

const routes = [
  {path: '/', exact: true, name: 'Garden', element: Garden},

  {path: '/me', exact: true, name: 'gardener',element: Gardener},

  {path: '/place', name: 'place', element: Place},
  {path: '/place/:placeId', name: 'place detail', element: PlaceDetail},

  {path: '/chemical', name: 'chemical', element: Chemical},
  {path: '/chemical/:chemicalId', name: 'chemical detail', element: ChemicalDetail},

  {path: '/plant', exact: true, name: 'plant', element: Plant},
  {path: '/plant/:plantId', exact: true, name: 'plant', element: PlantDetail},

  {path: '/schedule', exact: true, name: 'routine', element: Schedule},

  {path: '/watering', exact: true, name: 'watering', element: Watering},
  // {path: '/log', exact: true, name: 'log', element: Log},
]

export default routes
