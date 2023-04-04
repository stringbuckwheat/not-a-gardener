import React from 'react'
import { CFooter } from '@coreui/react'

const AppFooter = () => {
  return (
    <CFooter className="bg-white">
      <div>
        <span className="ms-1 small">Not A Gardener</span>
      </div>
      <div className="ms-auto">
        <span className="me-1 small">by stringbuckwheat</span>
      </div>
    </CFooter>
  )
}

export default React.memo(AppFooter)
