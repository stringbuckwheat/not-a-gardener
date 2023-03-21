import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react"
import chemicalTypeArray from "src/utils/dataArray/chemicalTypeArray"
import ChemicalTag from "src/components/tag/ChemicalTag";
import DetailLayout from "src/components/data/layout/DetailLayout";
import CIcon from "@coreui/icons-react";
import { cilPen, cilTrash } from "@coreui/icons";
import { Popconfirm, Space } from "antd";
import ItemForm from "src/components/form/ItemForm";
import ModifyFormButtons from "src/components/button/ModifyFormButtons";
import onMount from "src/api/service/onMount";
import ChemicalUsage from "./ChemicalUsage";
import deleteData from "src/api/backend-api/common/deleteData";


const ChemicalDetail = () => {
    const state = useLocation().state;
    console.log("ChemicalDetail state", state);

    const [wateringList, setWateringList] = useState([]);

    useEffect(() => {
        onMount(`/chemical/${state.chemicalNo}/watering-list`, setWateringList);
        console.log("wateringList", wateringList);
    }, []);

    const [chemical, setChemical] = useState(state);

    const onChange = (e) => {
        const { name, value } = e.target;
        setChemical(setChemical => ({ ...chemical, [name]: value }));
        console.log("onchange", chemical);
    }

    const itemObjectArray = [
        {
            inputType: "text",
            label: "이름",
            name: "chemicalName",
            defaultValue: state.chemicalName,
            required: true
        },
        {
            inputType: "select",
            label: "종류",
            name: "chemicalType",
            defaultValue: chemicalTypeArray[0].value,
            optionArray: chemicalTypeArray
        },
        {
            inputType: "number",
            label: "주기 (일)",
            name: "chemicalPeriod",
            defaultValue: state.chemicalPeriod,
            required: true
        }
    ]

    const validation = chemical.chemicalName != ''
        && Number.isInteger(chemical.chemicalPeriod * 1)
        && (chemical.chemicalPeriod * 1) > 0;

    const [onModify, setOnModify] = useState(false);

    const onClickModifyBtn = () => {
        setOnModify(!onModify);
    }

    const onClickModifyWatering = (record) => {
        console.log("onClickModifyWatering", record);
    }

    const wateringTableColumnArray = [
        {
            title: '언제',
            dataIndex: 'wateringDate',
            key: 'wateringDate'
        },
        {
            title: '식물 이름',
            dataIndex: 'plantName',
            key: 'plantName',
        },
        {
            title: '장소',
            dataIndex: 'placeName',
            key: 'placeName',
        },
        {
            title: '수정/삭제',
            key: '수정/삭제',
            render: (_, record) => (
                <>
                    <Space size='middle'>
                        <CIcon icon={cilPen} onClick={() => onClickModifyWatering(record)}/>
                        <Popconfirm
                            placement="topRight"
                            title="이 기록을 삭제하시겠습니까?"
                            description="삭제한 물 주기 정보는 되돌릴 수 없어요"
                            onConfirm={() => { confirm(record.wateringNo) }}
                            okText="네"
                            cancelText="아니요"
                        >
                            <CIcon icon={cilTrash} />
                        </Popconfirm>
                    </Space>
                </>
            ),
        },
    ]

    const confirm = (wateringNo) => {
        deleteData("/watering", wateringNo);
        const afterDelete = wateringList.filter(watering => watering.watering !== wateringNo);
        setWateringList(afterDelete);
    };

    const changeModifyState = () => {
        setOnModify(false);
    }

    return (
        !onModify
            ?
            <DetailLayout
                title={chemical.chemicalName}
                url="/chemical"
                path={state.chemicalNo}
                deleteTitle="비료/살균/살충제"
                tags={<ChemicalTag chemical={chemical} wateringListSize={wateringList.length} />}
                onClickModifyBtn={onClickModifyBtn}
                deleteTooltipMsg="삭제한 약품은 되돌릴 수 없습니다"
                bottomData={<ChemicalUsage
                    wateringTableColumnArray={wateringTableColumnArray}
                    wateringList={wateringList} />}
            />
            :
            <ItemForm
                title="비료/살균/살충제 수정"
                inputObject={chemical}
                itemObjectArray={itemObjectArray}
                onChange={onChange}
                submitBtn={<ModifyFormButtons
                    data={chemical}
                    url="chemical"
                    path={state.chemicalNo}
                    changeModifyState={changeModifyState}
                    validation={validation}/>} />
    )
}

export default ChemicalDetail;