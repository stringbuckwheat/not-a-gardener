import { useEffect, useState } from "react";
import { Select, Form, Space } from 'antd';
import { CButton } from "@coreui/react";
import getData from "src/api/backend-api/common/getData";
import { useNavigate, useParams } from "react-router-dom";
import modifyPlantPlace from "src/api/backend-api/place/modifyPlantPlace";

const AddPlantsInPlaceForm = () => {
    const placeNo = useParams().placeNo;

    const [visible, setVisible] = useState(false);
    const [options, setOptions] = useState([{}])

    const onMountAddPlantsInPlaceForm = async () => {
        const res = await getData("/plant");
        const exceptHere = res.filter(plant => plant.placeNo != placeNo);
        const options = exceptHere.map((plant) => (
            {
                label: `${plant.plantName} (${plant.placeName})`,
                value: plant.plantNo,
            }
        ))

        setOptions(options);
    }

    useEffect(() => {
        onMountAddPlantsInPlaceForm();
    }, [])

    const [ selectedPlantList, setSelectedPlantList ] = useState([]);

    const handleChange = async (value) => {
        setSelectedPlantList(value);
    };

    const navigate = useNavigate();

    const submit = async () => {
        // 장소 업데이트
        const data = { placeNo: placeNo, plantList: selectedPlantList }
        await modifyPlantPlace(data); // void

        const res = await getData(`/place/${placeNo}`)
        setVisible(false);
        navigate("", { replace: true, state: res })
    }

    return (
        <>
            {
                visible
                    ?
                    <Form className="mb-5" layout="vertical" autoComplete="off">
                        <Form.Item name="name" label="이 장소에 식물 추가하기" className="mb-2">
                            <Select
                                mode="multiple"
                                allowClear
                                onChange={handleChange}
                                options={options}
                            />
                        </Form.Item>
                        <Space className="float-end">
                            <CButton
                                size="sm"
                                variant="outline"
                                onClick={() => { setVisible(false) }}
                                color="dark">취소</CButton>
                            <CButton
                                size="sm"
                                variant="outline"
                                onClick={submit}
                                color="success">제출</CButton>
                        </Space>
                    </Form>
                    :
                    <CButton className="float-end mb-4" size="sm" variant="outline" onClick={() => { setVisible(true) }} color="success">이 장소에 식물 추가</CButton>
            }
        </>
    )
}

export default AddPlantsInPlaceForm;