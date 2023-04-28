import {CButton, CContainer, CImage, CRow} from "@coreui/react";
import React, {useState} from "react";
import forNoPlant from "../../assets/images/forNoPlant.png";

/**
 * 데이터 없음 페이지 or 추가하기 폼
 * @param title
 * @param buttonSize
 * @param buttonTitle
 * @param addForm 버튼 클릭 시 보여줄 추가 폼
 * @returns {JSX.Element}
 * @constructor
 */
const NoItem = ({title, buttonSize, buttonTitle, addForm}) => {
  const [isAddFormOpened, setIsAddFormOpened] = useState(false);

  return isAddFormOpened ? (
    <>
      {addForm}
    </>
  ) : (
    <CContainer fluid className="text-center">
      <CRow className="text-center mt-5">
        <h2>{title}</h2>
        <div className="d-grid gap-2 col-6 mx-auto mt-2">
          <CButton
            onClick={() => setIsAddFormOpened(true)}
            color="success"
            size={buttonSize}
            variant="outline"
            shape="rounded-pill">
            {buttonTitle}
          </CButton>
        </div>
      </CRow>
      <CRow>
        <CImage
          className="width-100 display-block"
          src={forNoPlant}
          fluid/>
      </CRow>
    </CContainer>
  )
}

export default NoItem;
