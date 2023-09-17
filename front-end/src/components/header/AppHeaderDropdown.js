import React from 'react'
import {
  CAvatar,
  CDropdown,
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
import {Space, Tag} from "antd";
import {CaretDownOutlined} from "@ant-design/icons";
import postData from "../../api/backend-api/common/postData";
import logOut from "../../utils/function/logout";

// account, setting 부분
const AppHeaderDropdown = () => {
  const logOutToServer = async () => {
    await postData("/gardener/logout", null);
    logOut();
  }

  return (
    <CDropdown variant="nav-item" >
      <CDropdownToggle placement="bottom-end" className="py-0" caret={false}>
        <div className="ms-3 d-none d-sm-block">
          <Space>
            <CAvatar src={sprout} size="sm"/>
            <Tag color="#305738" size="large">{localStorage.getItem("name")}</Tag>
          </Space>
          <CaretDownOutlined />
        </div>
      </CDropdownToggle>
      <CDropdownMenu className="pt-0" placement="bottom-end">
        <CDropdownItem href="/gardener" className="py-2">
          <CIcon icon={cilUser} className="me-2"/>
          개인정보 수정
        </CDropdownItem>
        <CDropdownItem onClick={logOutToServer}>
          <CIcon icon={cilLockUnlocked} className="me-2"/>
          로그아웃
        </CDropdownItem>
      </CDropdownMenu>
    </CDropdown>
  )
}

export default AppHeaderDropdown
