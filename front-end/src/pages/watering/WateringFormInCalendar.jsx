import {CCol, CRow, CContainer} from "@coreui/react";
import {Select, Space} from "antd";
import {useEffect, useState} from "react";
import GButton from "../../components/button/defaultButton/GButton";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import postData from "../../api/backend-api/common/postData";

const WateringFormInCalendar = ({plantList, chemicalList, isWateringFormOpened, setIsWateringFormOpened, onAdd, selectedDate}) => {
  const [watering, setWatering] = useState({});

  useEffect(() => {
    if (plantList && chemicalList) {
      setWatering({
        chemicalId: chemicalList.at(0)?.value,
        plantId: plantList.at(0)?.value,
        wateringDate: selectedDate.toISOString().split("T").at(0)
      })
    }
  }, [plantList, chemicalList]);

  if (!isWateringFormOpened) {
    return <></>;
  }

  const submit = async () => {
    console.log("watering", watering);
    const res = await postData("/watering", watering);
    onAdd(res);
    setIsWateringFormOpened(false);
  }

  const onChangeChemical = (value) => {
    setWatering({...watering, chemicalId: value})
    console.log("watering", watering);
  }

  return (
    <CContainer className="mt-4 mb-2 d-flex justify-content-center">
      <CRow>
        <h6 className="width-full">물주기 추가</h6>
        <CRow className="mt-1 mb-1">
          <CCol md={6} xs={12}>
            <small>누구한테 주었나요?</small>
            <Select
              className="width-full"
              defaultValue={plantList[0].value}
              onChange={(value) => setWatering({...watering, plantId: value})}
              options={plantList}
              name="chemicalNo"
            />
          </CCol>
          <CCol md={6} xs={12}>
            <small>무엇을 주었나요?</small>
            <Select
              className="width-full"
              defaultValue="맹물"
              onChange={onChangeChemical}
              options={chemicalList}
              name="chemicalNo"
            />
          </CCol>
        </CRow>
        <div className="mt-3 d-flex justify-content-end">
          <Space>
            <GButton color="dark" size="small" onClick={() => setIsWateringFormOpened(false)}>취소</GButton>
            <ValidationSubmitButton
              isValid={true}
              onClickValid={submit}
              onClickInvalidMsg={""}
              title={"제출"}
              size={"small"}
            />
          </Space>
        </div>
      </CRow>
    </CContainer>
  )
}

export default WateringFormInCalendar
