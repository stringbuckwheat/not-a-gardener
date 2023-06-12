import {CCol, CCard, CCardBody} from "@coreui/react";
import ForgotAccountCard from "../../../components/card/ForgotAccountCard";
import VerifyAccountContent from "./VerifyAccountContent";
import React from "react";

/**
 * 아이디/비밀번호 찾기의 첫 단계인 이메일 인증 페이지
 * 부모 컴포넌트: ForgotAccount
 * -> VerifyAccountContent
 * @param icon
 * @param title
 * @param setEmail
 * @param setGardenerList
 * @param successContent
 * @returns {JSX.Element}
 * @constructor
 */
const ValidateGardener = ({icon, title, setEmail, setGardenerList, successContent}) => {
  return (
    <>
      <CCol md={3}>
        <ForgotAccountCard
          color="orange"
          icon={icon}
          iconSize="6xl"
          buttonSize="sm"
          title={title}/>
      </CCol>
      <CCard
        style={{minHeight: "50vh", height: "100%"}}
        className={`bg-beige`}>
        <CCardBody className="d-flex align-items-center justify-content-center">
        <VerifyAccountContent
          setEmail={setEmail}
          setGardenerList={setGardenerList}
          successContent={successContent}/>
        </CCardBody>
      </CCard>
    </>
  )
}

export default ValidateGardener;
