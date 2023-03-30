import {CButton, CForm, CFormInput, CInputGroup, CInputGroupText, CRow} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import {cilLockLocked, cilUser} from "@coreui/icons";
import React from "react";

const LoginForm = (props) => {
  const {inputCheck, onChange} = props;

  return (
    <CRow>
      <CForm onSubmit={inputCheck} method="POST">
        <CInputGroup className="mb-3">
          <CInputGroupText>
            <CIcon icon={cilUser}/>
          </CInputGroupText>
          <CFormInput placeholder="ID" name="username" onChange={onChange}/>
        </CInputGroup>
        <CInputGroup className="mb-4">
          <CInputGroupText>
            <CIcon icon={cilLockLocked}/>
          </CInputGroupText>
          <CFormInput
            name="pw"
            type="password"
            placeholder="PW"
            onChange={onChange}
          />
        </CInputGroup>
        <div>
          <CButton size="sm" type="submit" className="px-4 float-end">로그인</CButton>
        </div>
        {/*<QuestionCircleOutlined className="float-end font-size-20"/>*/}
      </CForm>
    </CRow>
  )
}

export default LoginForm;
