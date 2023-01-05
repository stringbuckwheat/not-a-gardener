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
  cilSettings,
  cilUser,
} from '@coreui/icons'
import CIcon from '@coreui/icons-react'
import { useNavigate } from 'react-router-dom'
import sprout from './../../assets/images/sprout.png'

// account, setting 부분
const AppHeaderDropdown = () => {
  const navigate = useNavigate();

  const logOut = () => {
    localStorage.removeItem("login");
    navigate("/");
  }

  return (
    <CDropdown variant="nav-item">
      <CDropdownToggle placement="bottom-end" className="py-0" caret={false}>
        <CAvatar src={sprout} size="md" />
      </CDropdownToggle>
      <CDropdownMenu className="pt-0" placement="bottom-end">
        <CDropdownHeader className="bg-light fw-semibold py-2">Settings</CDropdownHeader>
        <CDropdownItem href="#">
          <CIcon icon={cilUser} className="me-2" />
          개인정보 수정
        </CDropdownItem>
        <CDropdownItem href="#">
          <CIcon icon={cilSettings} className="me-2" />
          설정
        </CDropdownItem>
        <CDropdownDivider />
        <CDropdownItem onClick={logOut}>
          <CIcon icon={cilLockUnlocked} className="me-2" />
          로그아웃
        </CDropdownItem>
      </CDropdownMenu>
    </CDropdown>
  )
}

export default AppHeaderDropdown
