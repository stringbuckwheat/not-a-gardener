import {CButton, CContainer, CImage, CRow} from "@coreui/react";
import React, {useState} from "react";
import forNoPlant from "../../assets/images/forNoPlant.png";
import getPlaceListForSelect from "../../api/service/getPlaceListForSelect";
import AddPlant from "../../pages/plant/AddPlant";

/**
 * 식물 없음 페이지
 * @param addPlant
 * @param afterAdd
 * @returns {JSX.Element}
 * @constructor
 */
const NoItemForPlant = ({addPlant, afterAdd}) => {
  const [isAddFormOpened, setIsAddFormOpened] = useState(false);
  const [placeList, setPlaceList] = useState([]);

  const onClick = async () => {
    const placeList = await getPlaceListForSelect();
    setPlaceList(() => placeList);
    setIsAddFormOpened(true);
  }

  return isAddFormOpened ? (
    <AddPlant placeList={placeList} addPlant={addPlant} afterAdd={afterAdd}/>
  ) : (
    <CContainer fluid className="text-center">
      <CRow className="text-center mt-5">
        <h2>등록된 식물이 없어요</h2>
        <div className="d-grid gap-2 col-6 mx-auto mt-2">
          <CButton
            onClick={onClick}
            color="success"
            size={"lg"}
            variant="outline"
            shape="rounded-pill">
            식물 등록하기
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

export default NoItemForPlant;
