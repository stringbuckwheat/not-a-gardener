import React from 'react'
import {
  CAvatar,
  CDropdown,
  CDropdownDivider,
  CDropdownHeader,
  CDropdownItem,
  CDropdownMenu,
  CDropdownToggle,
} from '@coreui/react'
import {
  cilLockUnlocked,
  cilUser,
} from '@coreui/icons'
import CIcon from '@coreui/icons-react'
import sprout from './../../assets/images/sprout.png'
import LogOut from 'src/utils/function/logout'

// account, setting 부분
const AppHeaderDropdown = () => {
  const memberUrl = "member/" + localStorage.getItem("memberNo");

  return (
    <CDropdown variant="nav-item">
      <CDropdownToggle placement="bottom-end" className="py-0" caret={false}>
        <CAvatar src={sprout} size="md" />
      </CDropdownToggle>
      <CDropdownMenu className="pt-0" placement="bottom-end">
        <CDropdownHeader className="bg-light fw-semibold py-2">Settings</CDropdownHeader>
        <CDropdownItem href={memberUrl}>
          <CIcon icon={cilUser} className="me-2" />
          개인정보 수정
        </CDropdownItem>
        {/* <CDropdownItem >
          <CIcon icon={cilSettings} className="me-2" />
          설정
        </CDropdownItem> */}
        <CDropdownDivider />
        <CDropdownItem onClick={LogOut}>
          <CIcon icon={cilLockUnlocked} className="me-2" />
          로그아웃
        </CDropdownItem>
      </CDropdownMenu>
    </CDropdown>
  )
}

export default AppHeaderDropdown
