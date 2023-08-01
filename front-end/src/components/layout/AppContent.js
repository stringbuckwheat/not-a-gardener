import React, { Suspense } from 'react'
import { Navigate, Route, Routes } from 'react-router-dom'
import { CContainer, CSpinner } from '@coreui/react'

// routes config
import routes from '../../utils/routes'
import Loading from "../data/Loading";
import NotFound from "../../pages/NotFound";

const AppContent = () => {
  return (
    <CContainer lg>
      <Suspense fallback={<Loading />}>
        <Routes>
          {routes.map((route, idx) => {
            return (
              route.element && (
                <Route
                  key={idx}
                  path={route.path}
                  exact={route.exact}
                  name={route.name}
                  element={<route.element />}
                />
              )
            )
          })}
          <Route path="/" element={<Navigate to="garden" replace />} />
          <Route path={"*"} element={<NotFound/>} />
        </Routes>
      </Suspense>
    </CContainer>
  )
}

export default React.memo(AppContent)
