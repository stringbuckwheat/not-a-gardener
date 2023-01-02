import React from 'react'
import { CFooter } from '@coreui/react'

const AppFooter = () => {
  return (
    <CFooter>
      <div>
        <span className="ms-1">Not A Gardener</span>
      </div>
      <div className="ms-auto">
        <span className="me-1">by stringbuckwheat</span>
      </div>
    </CFooter>
  )
}

export default React.memo(AppFooter)
