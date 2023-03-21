import { CContainer } from "@coreui/react";
import CIcon from "@coreui/icons-react";
import { cilPen, cilTrash, cilX } from "@coreui/icons";
import getWateringNotificationMsg from "src/utils/function/getWateringNotificationMsg";
import getData from "src/api/backend-api/common/getData";
import WateringFormOpen from "./WateringFormOpen";
import deleteData from "src/api/backend-api/common/deleteData";
import { useEffect, useState } from "react";

import locale from 'antd/es/date-picker/locale/ko_KR';
import 'dayjs/locale/ko';
import { Form, Table, notification, Popconfirm, Space, Button, Typography, DatePicker, Select, Input, } from "antd";

import dayjs from "dayjs";
import advancedFormat from 'dayjs/plugin/advancedFormat'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import localeData from 'dayjs/plugin/localeData'
import weekday from 'dayjs/plugin/weekday'
import weekOfYear from 'dayjs/plugin/weekOfYear'
import weekYear from 'dayjs/plugin/weekYear'
import getChemicalListForSelect from "src/api/service/getChemicalListForSelect";
import insertData from "src/api/backend-api/common/insertData";
import updateData from "src/api/backend-api/common/updateData";



/**
 * plant detail 아래쪽에 table로 들어감
 * @param {*} props 
 * @returns 
 */

const WateringList = (props) => {
    dayjs.extend(customParseFormat)
    dayjs.extend(advancedFormat)
    dayjs.extend(weekday)
    dayjs.extend(localeData)
    dayjs.extend(weekOfYear)
    dayjs.extend(weekYear)

    const plant = props.plant;
    const wateringList = props.wateringList;
    const setWateringList = props.setWateringList;

    console.log("*** wateringList", wateringList);

    const [chemicalList, setChemicalList] = useState([]);

    useEffect(() => {
        getChemicalListForSelect(setChemicalList);
    }, [])

    // 미래에 물준다고 기록하기 방지
    const disabledDate = (current) => {
        return current && current.valueOf() > Date.now();
    }

    const [ editWatering, setEditWatering ] = useState({});

    const onChangeDate = (dateString) => {
        setEditWatering({...editWatering, wateringDate: dateString})
    }

    const EditableCell = ({
        editing,
        dataIndex,
        title,
        inputType,
        record,
        index,
        children,
        ...restProps
    }) => {
        const inputNode = inputType === 'date'
            ? <DatePicker 
                    locale={locale} 
                    disabledDate={disabledDate} 
                    onChange={(date, dateString) => {onChangeDate(dateString)}} />
            : <Select 
                    options={chemicalList} 
                    onChange={(value) => { setEditWatering({...editWatering, chemicalNo: value}) }}/>;
        return (
            <td {...restProps}>
                {editing
                    ? (
                        <Form.Item
                            name={dataIndex}
                            style={{ margin: 0 }}
                            rules={[{ required: true, message: "날짜를 입력해주세요", },]}
                        >
                            {inputNode}
                        </Form.Item>
                    ) : (
                        children
                    )}
            </td>
        );
    };

    // editable rows
    const [form] = Form.useForm();
    // const [data, setData] = useState(wateringList);
    const [editingKey, setEditingKey] = useState('');
    const isEditing = (record) => record.wateringNo === editingKey;

    const edit = (record) => {
        console.log("record", record);

        form.setFieldsValue({
            ...record,
            wateringDate: dayjs(new Date(record.wateringDate)),
        });

        setEditWatering({
            wateringNo: record.wateringNo,
            wateringDate: record.wateringDate,
            chemicalNo: record.chemicalNo,
        });

        setEditingKey(record.wateringNo);
    };

    const cancel = () => {
        setEditingKey('');
    };

    const save = async (key) => {
        const data = await updateData("watering", editWatering.wateringNo, editWatering);
    };

    ///////////

    const wateringTableColumnArray = [
        {
            title: '언제',
            dataIndex: 'wateringDate',
            key: 'wateringDate',
            editable: true,
        },
        {
            title: '무엇을',
            dataIndex: 'chemicalName',
            key: 'chemicalName',
            editable: true,
        },
        {
            title: '관수 간격',
            dataIndex: 'wateringPeriod',
            key: 'wateringPeriod',
        },
        {
            title: '',
            key: 'action',
            render: (_, record) => {
                console.log("Render 내부 record", record)
                const editable = isEditing(record);

                return editable ? (
                    <Space className="d-flex justify-content-end">
                        <Button onClick={() => save(record.wateringNo)} size="small">
                            <CIcon className="text-success" icon={cilPen} />
                        </Button>
                        <Popconfirm title="Sure to cancel?" onConfirm={cancel}>
                            <Button size="small">
                                <CIcon icon={cilX} />
                            </Button>
                        </Popconfirm>
                    </Space>
                ) : (
                    <Space className="d-flex justify-content-end">
                        <Button size="small" disabled={editingKey !== ''} onClick={() => edit(record)}>
                            <CIcon className="text-success" icon={cilPen} />
                        </Button>
                        <Popconfirm
                            placement="topRight"
                            title="이 기록을 삭제하시겠습니까?"
                            description="삭제한 물 주기 정보는 되돌릴 수 없어요"
                            onConfirm={() => { confirm(record.wateringNo) }}
                            okText="네"
                            cancelText="아니요"
                        >
                            <Button size="small" disabled={editingKey !== ''} >
                                <CIcon icon={cilTrash} />
                            </Button>
                        </Popconfirm>
                    </Space>

                )
            },
        },
    ]

    const mergedColumns = wateringTableColumnArray.map((col) => {
        if (!col.editable) {
            return col;
        }

        return {
            ...col,
            onCell: (record) => ({
                record,
                inputType: col.dataIndex === 'wateringDate' ? 'date' : 'select',
                dataIndex: col.dataIndex,
                title: col.title,
                editing: isEditing(record),
            }),
        };
    });

    //////////////////////////

    const confirm = async (wateringNo) => {
        await deleteData("/watering", wateringNo);

        const res = await getData(`/plant/${plant.plantNo}/watering`);
        setWateringList(res);
    };

    // 물주기 추가/수정/삭제 후 메시지
    const [api, contextHolder] = notification.useNotification();
    const openNotification = (wateringMsg) => {
        const msg = getWateringNotificationMsg(wateringMsg.wateringCode)

        api.open({
            message: msg.title,
            description: msg.content,
            duration: 4,
        });
    };

    return (
        <>
            {contextHolder}

            <CContainer>
                <div className="mt-4 mb-3">
                    <WateringFormOpen
                        plantNo={plant.plantNo}
                        openNotification={openNotification}
                        wateringList={wateringList}
                        setWateringList={setWateringList}
                    />
                </div>
                <Form form={form} component={false}>
                    <Table
                        components={{
                            body: { cell: EditableCell, },
                        }}
                        pagination={{ onChange: cancel, }}
                        rowClassName="editable-row"
                        // columns={wateringTableColumnArray}
                        columns={mergedColumns}
                        dataSource={wateringList.map((watering) => {
                            return ({
                                wateringNo: watering.wateringNo,
                                wateringDate: watering.wateringDate,
                                chemicalNo: watering.chemicalNo,
                                chemicalName: watering.chemicalName,
                                wateringPeriod: watering.wateringPeriod == 0 ? "" : `${watering.wateringPeriod}일만에`
                            })
                        })}
                    />
                </Form>
            </CContainer>
        </>
    )
}

export default WateringList;