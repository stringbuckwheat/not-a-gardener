import {Card, Col, Row, Select, Space} from "antd";
import React, {useEffect, useState} from "react";
import GButton from "../../components/button/GButton";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import postData from "../../api/backend-api/common/postData";
import ExceptionCode from "../../utils/code/exceptionCode";

// TODO WateringForm과 같음
const WateringFormInCalendar = ({
                                  plantList,
                                  chemicalList,
                                  isWateringFormOpened,
                                  setIsWateringFormOpened,
                                  onAdd,
                                  selectedDate
                                }) => {
  const [watering, setWatering] = useState({});

  useEffect(() => {
    console.log("selectedDate", selectedDate.toISOString().split("T").at(0));
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
    console.log("submit watering", watering);

    try {
      const res = await postData("/watering", watering);
      onAdd(res);
      setIsWateringFormOpened(false);
    } catch (e) {
      if (e.code == ExceptionCode.ALREADY_WATERED) {
        alert(e.message);
      }
    }
  }

  const onChangeChemical = (value) => {
    setWatering({...watering, chemicalId: value})
    console.log("watering", watering);
  }

  return (
    <Row style={{justifyContent: "center", margin: "1rem 0"}}>
      <Col md={12}>
        <Card
          style={{borderColor: "green"}}>
          <h6>물주기 추가</h6>
          <Row>
            <Col md={12} xs={24}>
              <small>누구한테 주었나요?</small>
              <Select
                className="width-full"
                defaultValue={plantList[0].value}
                style={{width: "90%",}}
                onChange={(value) => setWatering({...watering, plantId: value})}
                options={plantList}
                name="plantIds"
              />
            </Col>
            <Col md={12} xs={24}>
              <small>무엇을 주었나요?</small>
              <Select
                className="width-full"
                defaultValue="맹물"
                style={{width: "90%",}}
                onChange={onChangeChemical}
                options={chemicalList}
                name="chemicalId"
              />
            </Col>
          </Row>

          <div style={{marginTop: "1rem", display: "flex", justifyContent: "flex-end"}}>
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
        </Card>
      </Col>
    </Row>
  )
}

export default WateringFormInCalendar
