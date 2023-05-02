import {CButton, CCol, CContainer, CRow} from "@coreui/react";
import {Select, Form, Space, DatePicker} from "antd";
import {useEffect, useState} from "react";
import locale from 'antd/es/date-picker/locale/ko_KR';
import 'dayjs/locale/ko';
import getData from "src/api/backend-api/common/getData";
import {useParams} from "react-router-dom";
import authAxios from "src/api/interceptors";
import getDisabledDate from "src/utils/function/getDisabledDate";

const AddChemicalUsage = ({setOnAdd,}) => {
  const chemicalNo = useParams().chemicalNo;

  const [options, setOptions] = useState([]);
  const [selectedPlantList, setSelectedPlantList] = useState([]);
  const [usageDate, setUsageDate] = useState("");

  const onMountAddChemicalUsage = async () => {
    const res = await getData("/plant");
    const option = res.map((plant) => (
      {
        label: `${plant.name} (${plant.name})`,
        value: plant.id,
      }
    ))

    setOptions(option);
  }

  useEffect(() => {
    onMountAddChemicalUsage();
  }, [])

  const handleChange = async (value) => {
    setSelectedPlantList(value);
  };

  const submit = () => {
    const data = selectedPlantList.map((plant) => ({
      chemicalId: chemicalNo,
      plantId: plant,
      wateringDate: usageDate
    }))

    authAxios.post("/watering/list", data);
  }

  return (
    <>
      <CContainer className="mb-3">
        <CRow className="d-flex justify-content-center mt-3">
          <CCol md={10} xs={12} className="rounded bg-light">
            <p className="text-success fw-bold mt-1"> 사용 내역 추가</p>
            <Select
              mode="multiple"
              className="mb-1 width-full"
              placeholder="식물을 선택해주세요"
              allowClear
              onChange={handleChange}
              options={options}
            />
            <DatePicker
              className="mb-2 width-full"
              disabledDate={getDisabledDate}
              onChange={(date, dateString) => {
                setUsageDate(dateString)
              }}
              locale={locale}
            />
            <Space className="float-end mb-1">
              <CButton
                size="sm"
                variant="outline"
                onClick={() => setOnAdd(false)}
                color="dark">취소</CButton>
              {
                selectedPlantList.length == 0 || usageDate === ""
                  ? <CButton size="sm" variant="outline" color="dark">제출</CButton>
                  : <CButton size="sm" variant="outline" onClick={submit} color="success">제출</CButton>
              }
            </Space>
          </CCol>
        </CRow>

      </CContainer>
    </>
  )
}

export default AddChemicalUsage;
