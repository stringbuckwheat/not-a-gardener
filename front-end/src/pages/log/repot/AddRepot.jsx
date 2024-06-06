import {CloseOutlined, PlusOutlined} from "@ant-design/icons";
import React, {useEffect, useState} from "react";
import {Button, Card, Col, ConfigProvider, DatePicker, Row} from "antd";
import getData from "../../../api/backend-api/common/getData";
import AddRepotForm from "./AddRepotForm";
import getDisabledDate from "../../../utils/function/getDisabledDate";
import dayjs from "dayjs";
import locale from "antd/es/date-picker/locale/ko_KR";
import themeGreen from "../../../theme/themeGreen";
import Loading from "../../../components/data/Loading";
import postData from "../../../api/backend-api/common/postData";

const AddRepot = ({onClickRepotFormButton}) => {
  const [loading, setLoading] = useState(true);
  const [repotDate, setRepotDate] = useState(dayjs().format("YYYY-MM-DD"));
  const [plantList, setPlantList] = useState([]);
  const [repotForms, setRepotForms] = useState([0]); // Keep track of form instances

  const onMountRepot = async () => {
    const plantList = await getData("/plant");
    const plantListForSelect = plantList.map((plant) => ({label: plant.name, value: plant.id}));
    setPlantList(() => plantListForSelect);
    setLoading(false);
  }

  useEffect(() => {
    onMountRepot();
  }, [])

  const addNewRepotForm = () => {
    setRepotForms((prevForms) => [...prevForms, prevForms.length]);
  };

  const submit = async () => {
    if (!isValid()) {
      alert("입력 데이터를 확인해주세요");
      return;
    }

    const repotData = repotForms.map((form, index) => ({
      repotDate: repotDate,
      plantId: form.plantId,
      haveToInitPeriod: form.haveToInitPeriod,
    }));

    console.log("Submit data:", repotData);

    try {
      const res = await postData("/repot", repotData);
      console.log("res", res);
    } catch (e) {
      alert(e.response.data.message);
    }
  };

  const isValid = () => {
    return (
      new Date(repotDate) <= new Date() &&
      repotForms.every((form) => form.plantId && (form.haveToInitPeriod === "Y" || form.haveToInitPeriod === "N"))
    );
  };

  const formLabelColor = "garden";
  const formStyle = {fontSize: "0.9rem"}

  return loading ? (
    <Loading/>
  ) : (
    <Card style={{marginBottom: "1rem", width: "40vh"}} bodyStyle={{padding: "1rem 0.3rem"}}>
      <div style={{marginBottom: "1rem",}}>
        <span style={{fontSize: "1rem"}} className="text-orange">분갈이 기록 추가</span>
        <CloseOutlined className="float-end" style={{color: "grey"}} onClick={onClickRepotFormButton}/>
      </div>

      {/* 분갈이 날짜 */}
      <div style={{marginBottom: "1.5rem"}}>
        <span className={`text-garden`} style={{fontSize: "0.9rem"}}>{"분갈이 날짜"}</span>
        <DatePicker
          name="wateringDate"
          style={{width: "100%",}}
          disabledDate={getDisabledDate}
          format={"YYYY-MM-DD"}
          value={dayjs(repotDate, "YYYY-MM-DD")}
          onChange={(date, dateString) => setRepotDate(() => dateString)}
          locale={locale}/>
      </div>

      <Row>
        <Col xs={24} md={12} style={{marginBottom: "1rem", paddingRight: "0.5rem"}}>
          <span className={`text-${formLabelColor}`} style={formStyle}>{"식물"}</span>
        </Col>
        <Col xs={24} md={12} style={{marginBottom: "1rem", paddingLeft: "0.5rem"}}>
          <span className={`text-${formLabelColor}`} style={formStyle}>물주기 초기화</span>
        </Col>
      </Row>

      {repotForms.map((formId, index) => (
        <AddRepotForm
          key={formId}
          plantList={plantList}
          onChange={(newData) => {
            setRepotForms((prevForms) => {
              const newForms = [...prevForms];
              newForms[index] = newData;
              return newForms;
            });
          }}
        />
      ))}

      <ConfigProvider theme={themeGreen}>
        <div style={{display: "flex", justifyContent: "flex-end", marginTop: "1rem"}}>
          <Button
            icon={<PlusOutlined/>}
            onClick={addNewRepotForm}
          >
            식물 더 추가하기
          </Button>
          <Button type="primary" onClick={submit} style={{marginLeft: "0.5rem"}}>
            제출
          </Button>
        </div>
      </ConfigProvider>
    </Card>
  )
}

export default AddRepot
