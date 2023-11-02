import React from 'react'
import {useSelector, useDispatch} from 'react-redux'
import {
  CAvatar,
  CContainer,
  CHeader,
  CHeaderToggler
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import {cilMenu} from '@coreui/icons'
import {Dropdown, Space, Tag} from "antd";
import {DownOutlined} from "@ant-design/icons";
import sprout from "../../assets/images/sprout.png";
import logOut from "../../utils/function/logout";
import {useNavigate} from "react-router-dom";

const AppHeader = () => {
  const dispatch = useDispatch();
  const sidebarShow = useSelector((state) => state.sidebar.sidebarShow);
  const name = useSelector(state => state.sidebar.name);
  const navigate = useNavigate();

  const items = [
    {
      key: 'edit',
      label: '회원 정보 수정',
      onClick: () => navigate("/me")
    },
    {
      key: 'logout',
      label: '로그아웃',
      onClick: logOut
    }
  ];

  return (
    <CHeader position="sticky" className="mb-3 bg-white">
      <CContainer fluid>
        <CHeaderToggler
          className="ps-1"
          onClick={() => dispatch({type: 'setSidebar', sidebarShow: !sidebarShow})}
        >
          <CIcon icon={cilMenu} size="lg"/>
        </CHeaderToggler>

        {/*유저 정보, 로그아웃 */}
        <Dropdown menu={{items}} trigger={['click']}>
          <Space>
            <CAvatar src={sprout} size="sm"/>
            <Tag color="#305738" size="large">{name}</Tag>
            <DownOutlined/>
          </Space>
        </Dropdown>

      </CContainer>
    </CHeader>
  )
}

export default AppHeader
