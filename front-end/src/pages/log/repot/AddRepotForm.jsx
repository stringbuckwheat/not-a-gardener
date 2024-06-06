import {Col, Radio, Row, Select, Space} from "antd";
import React, {useEffect, useState} from "react";

const AddRepotForm = ({plantList, onChange}) => {

  const [repot, setRepot] = useState({
    plantId: plantList[0]?.value,
    haveToInitPeriod: "Y"
  });

  const [plantIds, setPlantIds] = useState([]);

  const onChangePlant = (plantId) => {
    const newRepot = {...repot, plantId};
    setRepot(newRepot);
    onChange(newRepot);
  };

  const onChangePeriod = (e) => {
    const newRepot = {...repot, haveToInitPeriod: e.target.value};
    setRepot(newRepot);
    onChange(newRepot);
  };

  useEffect(() => {
    onChange(repot);
  }, []);

  return (
      <Row>
        {/* 식물들 */}
        <Col xs={24} md={12} style={{marginBottom: "1rem", paddingRight: "0.5rem"}}>
          <Select
            className="width-full"
            style={{width: "100%",}}
            defaultValue={plantList[0]?.value}
            onChange={onChangePlant}
            options={plantList}
            name="plantIds"
          />
        </Col>


        {/* 물주기 간격 초기화 */}
        <Col xs={24} md={12} style={{marginBottom: "1rem", paddingLeft: "0.5rem"}}>
          <Radio.Group
            style={{width: "100%", paddingTop: "0.2rem"}}
            onChange={onChangePeriod}
            value={repot.haveToInitPeriod}>
            <Space direction="horizontal">
              <Radio value={"Y"}>네</Radio>
              <Radio value={"N"}>아니요</Radio>
            </Space>
          </Radio.Group>
        </Col>
      </Row>
  )
}

export default AddRepotForm;
